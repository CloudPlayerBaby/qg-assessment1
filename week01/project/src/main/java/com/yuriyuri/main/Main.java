package com.yuriyuri.main;

import com.yuriyuri.pojo.Identity;
import com.yuriyuri.pojo.RepairForm;
import com.yuriyuri.pojo.User;
import com.yuriyuri.service.RepairFormService;
import com.yuriyuri.service.UserService;
import com.yuriyuri.service.impl.RepairFormServiceImpl;
import com.yuriyuri.service.impl.UserServiceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final UserService userService = new UserServiceImpl();
    private static final RepairFormService repairFormService = new RepairFormServiceImpl();
    private static final Scanner sc = new Scanner(System.in);

    //假装cookie
    private static int loginId;
    private static String loginSid;
    private static Identity loginIdentity;
    private static String loginDormitory;

    /**
     * main函数
     *
     * @param args
     */
    public static void main(String[] args) {
        menu:
        while (true) {
            //初始化菜单
            menu();
            String input = sc.nextLine();
            if (!input.matches("^[1-3]$")) {
                System.out.println("无效输入");
                continue;
            }

            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    System.out.println("正在退出");
                    break menu;
                }
            }

        }

    }

    /**
     * 主菜单
     */
    public static void menu() {
        System.out.println("===========================\n" +
                "\uD83C\uDFE0 宿舍报修管理系统\n" +
                "===========================\n" +
                "1. 登录\n" +
                "2. 注册\n" +
                "3. 退出\n" +
                "请选择操作（输入 1-3）：");
    }

    /**
     * 登录界面
     */
    public static void login() {
        //防止死循环就不用while了
        System.out.println("===== 用户登录 =====");

        System.out.println("请输入账号：");
        String sid = sc.nextLine();
        System.out.println("请输入密码：");
        String password = sc.nextLine();

        User user = userService.select(sid, password);

        if (user == null) {
            System.out.println("用户不存在，或工号/密码错误");
            return;
        }

        if (!sid.equals(user.getSid()) || !password.equals(user.getPassword())) {
            System.out.println("工号或密码错误");
            return;
        }

        //到这里说明用户存在，且工号和密码都输入正确
        loginId = user.getId();
        loginSid = user.getSid();
        loginIdentity = user.getIdentity();
        loginDormitory = user.getDormitory();

        Identity identity = user.getIdentity();
        if (identity == Identity.STUDENT) {
            System.out.println("登陆成功！角色：学生");
            studentMenu();
        } else if (identity == Identity.ADMIN) {
            System.out.println("登陆成功！角色：管理员");
            adminMenu();
        }
    }

    /**
     * 注册界面
     */
    public static void register() {
        while (true) {
            System.out.println("===== 用户注册 =====\n" +
                    "请选择角色（1-学生，2-维修人员）：");

            //如果为了增加用户体验，其实可以使用双层while防止某个值输错就全部重来，但是考虑到后续要搬到前端，这样做反而会导致到时候更难搬，所以就算了
            String identityInput = sc.nextLine();
            if (!identityInput.matches("^[1-2]$")) {
                System.out.println("请正确输入");
                continue;
            }

            //获取身份
            Identity identity = "1".equals(identityInput) ? Identity.STUDENT : Identity.ADMIN;

            System.out.println("请输入工号：");
            String sid = sc.nextLine();

            System.out.println("请输入密码：");
            String password = sc.nextLine();
            System.out.println("请确认密码：");
            String _password = sc.nextLine();

            //在密码不为空的情况下
            if (!(password == null || _password == null || password.isEmpty() || _password.isEmpty())) {
                //如果两次输入的密码不一致，就不给注册
                if (!password.equals(_password)) continue;
            }

            if (userService.add(sid, password, identity)) {
                System.out.println("注册成功，即将跳转到登陆界面");
                login();
                break;
            }

        }

    }

    /**
     * 学生菜单
     */
    public static void studentMenu() {
        studentMenu:
        while (true) {
            if (!checkDormitory()) System.out.println("还没有绑定宿舍，请先绑定");
            System.out.println("===== 学生菜单 =====\n" +
                    "1. 绑定/修改宿舍\n" +
                    "2. 创建报修单\n" +
                    "3. 查看我的报修记录\n" +
                    "4. 取消报修单\n" +
                    "5. 修改密码\n" +
                    "6. 退出\n" +
                    "请选择操作（输入 1-6）：");

            String input = sc.nextLine();
            if (!input.matches("^[1-6]$")) {
                System.out.println("无效输入");
                continue;
            }

            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1 -> dormitory();
                case 2 -> createForm();
                case 3 -> checkMyRecord();
                case 4 -> cancelRepair();
                case 5 -> updatePassword();
                case 6 -> {
                    System.out.println("正在退出");
                    break studentMenu;
                }
            }

        }
    }

    /**
     * 检查是否绑定了宿舍
     *
     */
    public static boolean checkDormitory() {
        User userInfo = userService.selectById(loginId);

        //防空指针的检验
        if (userInfo == null) {
            System.out.println("无效登录");
            return false;
        }

        return userInfo.getDormitory() != null;
    }

    /**
     * 绑定/修改宿舍
     *
     */
    public static void dormitory() {
        dormitory:
        while (true) {
            System.out.println("1. 绑定宿舍\n" +
                    "2. 修改宿舍\n" +
                    "3. 退出\n" +
                    "请选择（输入1-3）：");
            String input = sc.nextLine();
            if (!input.matches("^[1-3]$")) {
                System.out.println("无效输入");
                continue;
            }

            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1 -> {
                    //如果绑定成功就退出
                    if (bindDormitory()) {
                        System.out.println("绑定成功");
                        break dormitory;
                    }
                }
                case 2 -> {
                    if (updateDormitory()) {
                        System.out.println("修改成功");
                        break dormitory;
                    }
                }
                case 3 -> {
                    System.out.println("正在退出");
                    break dormitory;
                }
            }

        }

    }

    /**
     * 绑定宿舍
     *
     */
    public static boolean bindDormitory() {
        //检查一下有没有宿舍了
        if (checkDormitory()) {
            System.out.println("已经绑定宿舍了！");
            return false;
        }
        System.out.println("请输入宿舍号：");
        String dormitory = sc.nextLine();

        if (dormitory == null || dormitory.isEmpty()) {
            System.out.println("宿舍号不能为空");
            return false;
        }

        userService.bindDormitory(dormitory, loginId);

        return true;
    }

    /**
     * 修改宿舍
     *
     */
    public static boolean updateDormitory() {
        if (!checkDormitory()) {
            System.out.println("还没有绑定宿舍！");
            return false;
        }

        System.out.println("原来的宿舍为：" + loginDormitory);
        System.out.println("你需要修改为：");
        String dormitory = sc.nextLine();

        if (dormitory == null || dormitory.isEmpty()) {
            System.out.println("宿舍号不能为空");
            return false;
        }

        if (dormitory.equals(loginDormitory)) {
            System.out.println("与原来的宿舍号一致，请重新输入");
            return false;
        }

        userService.updateDormitory(dormitory, loginId);

        return true;
    }

    /**
     * 创建保修单
     *
     */
    public static void createForm() {
        //检查是否有绑定宿舍，如果没有（false），则返回，其他方法同理
        if (!checkDormitory()) return;

        System.out.println("请选择你需要报修的的设备类型：\n" +
                "1. 空调\n" +
                "2. 门窗\n" +
                "3. 供水\n" +
                "请输入选择（输入1-3）：");
        String choice = sc.nextLine();
        if (!choice.matches("^[1-3]$")) {
            System.out.println("输入无效");
            return;
        }

        String type = "";
        switch (choice) {
            case "1" -> type = "空调";
            case "2" -> type = "门窗";
            case "3" -> type = "供水";
        }

        System.out.println("请输入你的问题（150字以内）：");
        String problem = sc.nextLine();

        if (problem == null || problem.isEmpty()) {
            System.out.println("问题不能为空");
            return;
        }

        if (problem.length() > 150) {
            System.out.println("字数超出限制");
            return;
        }

        repairFormService.createForm(loginId, type, problem);
        System.out.println("创建完毕");
    }

    /**
     * 查看报修记录
     */
    public static void checkMyRecord() {
        if (!checkDormitory()) return;
        List<RepairForm> repairForms = repairFormService.selectFormsByUid(loginId);

        System.out.println("报修单如下：");
        for (RepairForm repairForm : repairForms) {
            if (repairForm == null) {
                System.out.println("还没有报修哦...");
                return;
            }

            String type = repairForm.getType();
            int status = repairForm.getStatus();
            //其实还可以加一个受理中，但是参考我们学校自己的报修系统好像也无所谓了
            String statusStr = status == 0 ? "未完成" : "已完成";

            System.out.println("您报修的：" + type + "，目前状态为：" + statusStr);
        }

        System.out.println("输入1查看详情，输入2退出");
        String input = sc.nextLine();

        if (!input.matches("^[1-2]$")) {
            System.out.println("无效输入");
            return;
        }

        if ("2".equals(input)) {
            return;
        }

        checkDetail(loginId);
    }

    public static void checkDetail(int loginId) {
        if (!checkDormitory()) return;
        List<RepairForm> repairForms = repairFormService.selectFormsByUid(loginId);
        //本来我是想写能够让用户选择需要查看哪些详情，但这个个人的宿舍报修单四年也不会有几次吧。干脆全打印出来得了
        System.out.println("报修单详情如下：");
        for (RepairForm repairForm : repairForms) {
            if (repairForm == null) {
                return;
            }

            int id = repairForm.getId();
            String type = repairForm.getType();
            String problem = repairForm.getProblem();
            int status = repairForm.getStatus();
            //其实还可以加一个受理中，但是参考我们学校自己的报修系统好像也无所谓了
            String statusStr = status == 0 ? "未完成" : "已完成";

            Timestamp updateTime = repairForm.getUpdateTime();

            System.out.println("单号：" + id + "。您报修的：" + type + "，目前状态为：" + statusStr + "。报修时的问题如下：" + problem + "，最近一次的更新时间为：" + updateTime);
        }
        System.out.println("如有疑问，请致电114514");
    }

    /**
     * 取消保修单
     *
     */
    public static void cancelRepair() {
        checkDetail(loginId);
        //取消报修单就直接把受理状态变成1吧
        System.out.println("请输入你需要取消的报修单单号：");
        String idStr = sc.nextLine();

        RepairForm repairForm = checkFormById(idStr);

        if (repairForm == null) {
            return;
        }

        if (repairForm.getStatus() == 1) {
            System.out.println("该报修已经处理完毕，无法取消");
            return;
        }

        if (repairFormService.updateStatus(1, Integer.parseInt(idStr))) {
            System.out.println("取消报单成功");
        } else {
            System.out.println("未知错误");
        }
    }

    /**
     * 修改密码
     *
     */
    public static void updatePassword() {
        System.out.println("请输入你原来的密码：");
        String password = sc.nextLine();
        //简单校验
        if (password == null || password.isEmpty()) {
            System.out.println("密码不能为空");
            return;
        }

        //获取旧密码
        User userInfo = userService.selectById(loginId);
        String oldPassword = userInfo.getPassword();

        if (!password.equals(oldPassword)) {
            System.out.println("密码输入错误");
            return;
        }

        System.out.println("请输入新密码");
        String newPassword = sc.nextLine();
        System.out.println("请确认密码：");
        String _newPassword = sc.nextLine();

        //在密码不为空的情况下
        if (!(newPassword == null || _newPassword == null || newPassword.isEmpty() || _newPassword.isEmpty())) {
            //如果两次输入的密码不一致，就不给注册
            if (!newPassword.equals(_newPassword)) {
                System.out.println("两次输入的密码不一致");
                return;
            }
        }

        //密码一样不给你改
        if (oldPassword.equals(newPassword)) {
            System.out.println("新密码与旧密码一致，无法修改");
            return;
        }

        //坚持到最后就是胜利
        userService.updatePassword(newPassword, loginId);
        System.out.println("修改成功");
    }

    /**
     * 管理员菜单
     */
    public static void adminMenu() {
        adminMenu:
        while (true) {
            System.out.println("===== 管理员菜单 =====\n" +
                    "1. 查看所有报修单\n" +
                    "2. 查看报修单详情\n" +
                    "3. 更新报修单状态\n" +
                    "4. 删除报修单\n" +
                    "5. 修改密码\n" +
                    "6. 退出\n" +
                    "请选择操作（输入 1-6）：");
            String input = sc.nextLine();
            if (!input.matches("^[1-6]$")) {
                System.out.println("无效输入");
                continue;
            }

            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1 -> checkAll();
                case 2 -> checkOneDetail();
                case 3 -> updateStatus();
                case 4 -> deleteForm();
                case 5 -> updatePassword();
                case 6 -> {
                    System.out.println("退出");
                    break adminMenu;
                }
            }
        }
    }


    /**
     * 查看所有报修单
     */
    public static void checkAll() {
        if (!checkAtLeastOneForm()) return;
        System.out.println("输入1查看所有报修单，输入2查看未完成的报修单，输入3查看已完成的报修单\n" +
                "请输入选择（输入1-3）：");
        String input = sc.nextLine();

        if (!input.matches("^[1-3]$")) {
            System.out.println("无效输入");
            return;
        }

        int choice = Integer.parseInt(input);
        switch (choice) {
            case 1 -> {
                List<RepairForm> repairForms = repairFormService.selectAllForms();
                printAdminForms(repairForms);
            }

            case 2 -> {
                List<RepairForm> repairForms = repairFormService.selectFormsByStatus(0);
                if (repairForms == null || repairForms.isEmpty()) {
                    System.out.println("还没有未完成的报单哦...");
                    return;
                }
                printAdminForms(repairForms);
            }
            case 3 -> {
                List<RepairForm> repairForms = repairFormService.selectFormsByStatus(1);
                if (repairForms == null || repairForms.isEmpty()) {
                    System.out.println("还没有已完成的报单哦...");
                    return;
                }
                printAdminForms(repairForms);
            }

        }


    }

    /**
     * 查看报修单详情
     */
    public static int checkOneDetail() {
        if (!checkAtLeastOneForm()) return -1;
        System.out.println("请输入所需的报修单号");
        String input = sc.nextLine();

        RepairForm repairForm = checkFormById(input);

        if (repairForm == null) {
            return -1;
        }
        //获取学生信息

        int userId = repairForm.getUserId();
        User userInfo = userService.selectById(userId);

        if (userInfo == null) {
            System.out.println("报修人错误哦...");
            return -1;
        }

        List<RepairForm> repairForms = new ArrayList<>();
        repairForms.add(repairForm);

        printAdminForms(repairForms);
        System.out.println("该单的报修工号为：" + userInfo.getSid() + "。宿舍号为：" + userInfo.getDormitory());

        return Integer.parseInt(input);
    }

    /**
     * 更新报修单状态
     */
    public static void updateStatus() {
        if (!checkAtLeastOneForm()) return;
        ;
        System.out.println("请输入你需要修改状态的报修单单号：");

        String idStr = sc.nextLine();

        RepairForm repairForm = checkFormById(idStr);

        if (repairForm == null) {
            return;
        }

        int oldStatus = repairForm.getStatus();

        System.out.println("请输入你需要修改的状态\n" +
                "0. 未完成  1. 已完成\n" +
                "请输入选择(输入0-1)：");

        String statusStr = sc.nextLine();
        if (!statusStr.matches("[0-1]")) {
            System.out.println("无效输入");
            return;
        }

        int status = Integer.parseInt(statusStr);
        if (oldStatus == status) {
            System.out.println("受理状态一致，修改失败");
            return;
        }

        //坚持到最后就是胜利，可以修改了
        repairFormService.updateStatus(status, Integer.parseInt(idStr));
        System.out.println("修改成功");
    }

    /**
     * 删除报修单
     */
    public static void deleteForm() {
        if (!checkAtLeastOneForm()) return;
        System.out.println("⚠请问你真的要删除报单吗？这很危险⚠\n" +
                "输入1确认，输入2退出\n" +
                "请输入选择（输入1-2）：");

        String input = sc.nextLine();
        if (!input.matches("^[1-2]$")) {
            System.out.println("无效输入");
            return;
        }

        int choice = Integer.parseInt(input);
        if (choice == 2) {
            System.out.println("正在退出");
            return;
        }

        //到这里就算真的要删除了，先打印详情让管理员确认
        int id = checkOneDetail();
        if (id == -1) {
            return;
        }

        System.out.println("⚠请问你真的要删除报单吗？这很危险，这是最后一次确认⚠\n" +
                "输入1确认，输入2退出\n" +
                "请输入选择（输入1-2）：");

        String lastChoiceStr = sc.nextLine();
        if (!lastChoiceStr.matches("^[1-2]$")) {
            System.out.println("无效输入");
            return;
        }

        int lastChoice = Integer.parseInt(lastChoiceStr);
        if (lastChoice == 2) {
            System.out.println("正在退出");
            return;
        }

        User userInfo = userService.selectById(id);
        if (userInfo == null) {
            System.out.println("登录错误");
            return;
        }
        String realPassword = userInfo.getPassword();

        //要来咯！
        System.out.println("请输入密码：");
        String inputPassword = sc.nextLine();

        if (!realPassword.equals(inputPassword)) {
            System.out.println("密码错误");
            return;
        }

        //删了
        if (repairFormService.deleteById(id)) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败，未知错误");
        }

    }

    /**
     * 打印报修单（管理员）
     *
     * @param repairForms
     */
    public static void printAdminForms(List<RepairForm> repairForms) {
        System.out.println("查询的报修单如下：");
        for (RepairForm repairForm : repairForms) {
            if (repairForm == null) {
                System.out.println("还没有报单哦...");
                return;
            }

            int id = repairForm.getId();
            String type = repairForm.getType();
            String problem = repairForm.getProblem();
            int status = repairForm.getStatus();

            String statusStr = status == 0 ? "未完成" : "已完成";

            Timestamp updateTime = repairForm.getUpdateTime();

            System.out.println("单号：" + id + "。报修的类型：" + type + "，目前状态为：" + statusStr + "。报修时的问题如下：" + problem + "，最近一次的更新时间为：" + updateTime);
        }
    }

    /**
     * 根据输入的id查询是否存在报单（单个），如果存在则返回
     *
     * @param idStr
     * @return
     */
    public static RepairForm checkFormById(String idStr) {
        if (!idStr.matches("[1-9]\\d*")) {
            //如果非数字就别来了
            System.out.println("无效输入，请输入正整数");
            return null;
        }

        int id = Integer.parseInt(idStr);

        //根据输入的id查询这一个报单是否存在
        RepairForm repairForm = repairFormService.selectFormsById(id);
        if (repairForm == null) {
            System.out.println("单号不存在");
            return null;
        }
        return repairForm;
    }

    public static boolean checkAtLeastOneForm() {
        List<RepairForm> repairForms = repairFormService.selectAllForms();
        if (repairForms == null || repairForms.isEmpty()) {
            //如果一个报单都没有，就返回false
            System.out.println("还没有报修单哦，可以放假了");
            return false;
        }
        //否则返回true
        return true;
    }

}
