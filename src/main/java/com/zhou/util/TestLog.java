@Slf4j
public class TestLog {
    public static void main(String[] args) throws JsonProcessingException {

        /**
          * @Author JackZhou
          * @Description  动态设置日志级别，前置表达式前置执行
         **/
        LoggingSystem loggingSystem = LoggingSystem.get(ClassLoader.getSystemClassLoader());
        loggingSystem.setLogLevel(log.getName(), LogLevel.INFO);

        System.out.println(log.getName());

        log.info("测试打印:{}, 测试打印： {}", 1, 2);
        UserDomain userDomain = new UserDomain();
        userDomain.setUserName("张三");
        userDomain.setUserId(123456);
        log.debug("测试打印：{}, 测试打印表达式:{}", 1, JsonUtils.objectToJsonStr(userDomain));

        loggingSystem.setLogLevel(log.getName(), LogLevel.DEBUG);

        log.info("测试打印:{}, 测试打印： {}", 1, 2);
        log.debug("测试打印：{}, 测试打印表达式:{}", 1, JsonUtils.objectToJsonStr(userDomain));

    }

}

class UserDomain {
    private Integer userId;

    private String userName;

    private String password;

    private String phone;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
}
