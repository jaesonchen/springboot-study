# Swagger2
Swagger是一个功能强大的api框架，它的集成非常简单，不仅提供了在线文档的查阅，而且还提供了在线文档的测试。另外Swagger可以很容易构建restful风格的api。
    
Swagger2可以利用注解快速、自动地生成接口文档页面。
    

## Swagger2 依赖
```
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
</dependency>
```
    

## 启用 Swagger2
Springboot中使用`@EnableSwagger2`开启swagger2。
    

## Swagger2 配置
```
@Configuration
public class Swagger2Config implements WebMvcConfigurer {
    
    // swagger-ui.html文档展示页，还必须注入swagger资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.asiainfo.springboot.swagger"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot RESTful Api")
                .description("SpringBoot 集成 Swagger2 构建RESTful API")
                // 服务条款网址
                .termsOfServiceUrl("https://github.com/jaesonchen")
                // 创建人
                .contact(new Contact("jaesonchen", "https://github.com/jaesonchen", "jaesonchen@163.com"))
                .version("1.0")
                .build();
    }
}
```
    

## @ApiIgnore
Swagger2默认将所有的Controller中的RequestMapping方法都会暴露，然而在实际开发中，我们并不一定需要把所有API都出现在文档中，这种情况下，使用注解`@ApiIgnore`来忽略暴露API。
    
`@ApiIgnore` 可以标注在 类、方法和方法参数上。
    

## @Api
用于controller类上，表示对接口类的说明。
- `tags` 说明该类的用途的tag分类列表，用于文档控制时进行逻辑分组
- `value` 支持遗留的属性，不需要配置，当tags没有设置时，会使用该值作为tag
- `description` 对api资源的描述
    
```
@Api(value = "用户管理", tags = "用户管理模块")
@RequestMapping("/users")
@RestController
public class UserController { ... }
```
    

## @ApiOperation
用在请求的方法上，说明方法的功能。
- `value` 接口的功能
- `notes` 接口的备注说明
    
```
@ApiOperation(value = "删除", notes = "根据id删除用户")
@DeleteMapping("/{id}")
public ResponseEntity<Response> deleteById(@PathVariable long id, @RequestHeader String token) { ... }
```
    

## @ApiImplicitParams / @ApiImplicitParam
`@ApiImplicitParams`用在请求的方法上，包含一组参数说明。`@ApiImplicitParam`对单个参数的说明。
    
- name：参数
- value：参数的汉字说明、解释
- required：参数是否必须传
- paramType：参数通过哪种方式传递
    - header：通过请求头传递，参数的获取：@RequestHeader
    - query：通过query String传递，请求参数的获取：@RequestParam
    - path：用于restful接口，通过路径参数传递，请求参数的获取：@PathVariable
    - body：通过body传递，通常是application/json，请求参数的获取：@RequestBody
    - form：通过form传递，请求参数的获取：@RequestParam
- dataType：参数类型，默认String，其它值dataType="Integer"
- example：dataType使用非String类型时，最好设置example属性，否则容易报数据转换异常
- defaultValue：参数的默认值
    
```
@ApiOperation(value = "保存", notes = "新增用户")
@ApiImplicitParams({
    @ApiImplicitParam(name = "token", value = "token字符串", required = true, dataType = "string", paramType = "header"), 
    @ApiImplicitParam(name = "user", value = "json用户信息", required = true, dataType = "User", paramType = "body")})
@PostMapping
public ResponseEntity<Response> save(@RequestBody User user, @RequestHeader String token) { ... }
```
    

## @ApiResponses / @ApiResponse
`@ApiResponses`用于表示一组响应，`@ApiResponse`用于表达一个错误的响应信息。
- code：数字，响应编码，例如401/404
- message：响应编码说明，例如请求参数错误
- response：抛出异常的类
    
```
@Api(value = "用户管理", tags = "用户管理模块")
@ApiResponses({
    @ApiResponse(code = 200, message = "请求正常完成"),
    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
    @ApiResponse(code = 403, message = "服务器接受请求，但是拒绝处理"),
    @ApiResponse(code = 404, message = "请求路径不对"), 
    @ApiResponse(code = 500, message = "服务器出现异常")
})
@RequestMapping("/users")
@RestController
public class UserController {
```
    

## @ApiModel / @ApiModelProperty
`@ApiModel`用于模型类上，表示一个JavaBean模型的信息。`@ApiModelProperty` 用于描述一个JavaBean属性。
    
```
@ApiModel(description = "用户模型")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private long id;
    @ApiModelProperty(value = "登陆名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    
    ...
}
```

