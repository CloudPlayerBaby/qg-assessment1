package com.yuriyuri.controller;

import com.yuriyuri.common.Result;
import com.yuriyuri.dto.FormDTO;
import com.yuriyuri.entity.Identity;
import com.yuriyuri.entity.RepairForm;
import com.yuriyuri.service.RepairFormService;
import com.yuriyuri.util.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Tag(name = "报单管理", description = "报单相关接口")
@RestController
@RequestMapping("/repairForms")
public class RepairFormController {
    @Autowired
    private RepairFormService repairFormService;

    @Operation(summary = "创建报单",description = "需要类型、问题和图片地址")
    @PostMapping("/create")
    public Result<Void> createForm(@RequestParam String type,
                                   @RequestParam String problem,
                                   @RequestParam @URL String imageUrl) {

        //从 token获取用户id
        Map<String,Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        repairFormService.createForm(id, type, problem,imageUrl);
        return Result.success();
    }

    @Operation(summary = "通过学号查询报修单",description = "从token获取自己的学号，只能查自己的")
    @GetMapping("/mine")
    public Result<List<RepairForm>> selectFormsByUid() {
        //从 token获取用户id
        Map<String,Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        List<RepairForm> repairForms = repairFormService.selectFormsByUid(id);
        return Result.success(repairForms);
    }

    @Operation(summary = "更新报单状态",description = "管理员可随意更新，学生只可以取消")
    @PatchMapping("/{id}/status")
    public Result<Boolean> updateStatus(@PathVariable int id, @RequestBody FormDTO dto){
        //更新表单状态，如果是学生只能把0改1，如果是管理员可以进行其他操作
        Map<String,Object> map = ThreadLocalUtil.get();
        String identity = (String) map.get("identity");
        int userId = (int) map.get("id");
        if("ADMIN".equals(identity)){
            //如果是管理员，可以修改所有报单的状态
            boolean result = repairFormService.updateStatus(dto.getStatus(), id);
            return Result.success(result);
        }else {
            //如果是小登，只能取消自己的报单
            RepairForm repairForm = repairFormService.selectFormsById(id);
            if(repairForm.getUserId()!=userId){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"无权限");
            }
            if(repairForm.getStatus()!=0){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"只能取消报单");
            }
            //坚持到最后就是胜利，把报单状态改为1就相当于取消了
            boolean result = repairFormService.updateStatus(1, id);
            return Result.success(result);
        }

    }

    @Operation(summary = "通过id查询报单")
    @GetMapping("/{id}")
    public Result<RepairForm> selectFormsById(@PathVariable int id){
        RepairForm repairForm = repairFormService.selectFormsById(id);
        return Result.success(repairForm);
    }

    @Operation(summary = "查询所有报单")
    @GetMapping("/all")
    public Result<List<RepairForm>> selectAllForms(){
        List<RepairForm> repairForms = repairFormService.selectAllForms();
        return Result.success(repairForms);
    }

    @Operation(summary = "根据状态查询报单",description = "目前只有0和1")
    @GetMapping("/status")
    public Result<List<RepairForm>> selectFormsByStatus(@RequestParam int status){
        List<RepairForm> repairForms = repairFormService.selectFormsByStatus(status);
        return Result.success(repairForms);
    }

    @Operation(summary = "根据id删除报单",description = "只有管理员能删")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable int id){
        Map<String,Object> map = ThreadLocalUtil.get();
        String identity = (String) map.get("identity");
        //身份校验，只有管理员才能删除
        if(!"ADMIN".equals(identity)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"无权限");
        }

        boolean result = repairFormService.deleteById(id);
        return Result.success(result);
    }
}
