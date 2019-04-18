package com.asiainfo.springboot.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @SpringBootApplication 等价于 @EnableAutoConfiguration、 @Configuration、 @ComponentScan
 * 
 * @EnableAutoConfiguration只应该设置一个，建议添加到主@Configuration 类上。exclude={DataSourceAutoConfiguration.class} 用于disable 某个@Configuration类。
 * @EnableAutoConfiguration 会自动激活@DataSourceAutoConfiguration，HibernateJpaAutoConfiguration配置数据源，HelloWorld示例不配置数据源时需要exclude。
 * @ComponentScan 不指定扫描范围时，以当前类所在的包为base package
 * 
 * 
 * @ConditionalOnClass 如果classpath中存在指定的类型，则实例化该类型。
 * @ConditionalOnProperty 如果properties属性中的值符合预期，则实例化该类型。
 * @ConditionalOnSingleCandidate 如果容器里存在该类型唯一的bean，则实例化该类型。
 * @ConditionalOnMissingBean 如果容器里不存在指定类型的bean，则实例化该类型。
 * @ConditionalOnBean 如果容器里存在该类型bean，则实例化该类型。
 * 
 * 
 * @EnableAutoConfiguration 激活的autoconfigure ：
 *  AopAutoConfiguration --> @ConditionalOnClass({ EnableAspectJAutoProxy.class, Aspect.class, Advice.class })
 *                           @ConditionalOnProperty(prefix = "spring.aop", name = "auto", havingValue = "true", matchIfMissing = true)
 *                       --> @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false", matchIfMissing = true)
 *                           @EnableAspectJAutoProxy(proxyTargetClass = false)
 *                       --> @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = false)
 *                           @EnableAspectJAutoProxy(proxyTargetClass = true)
 *  
 *  ConfigurationPropertiesAutoConfiguration --> @EnableConfigurationProperties
 *  
 *  DataSourceAutoConfiguration --> @ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class }) 
 *                              --> @EnableConfigurationProperties(DataSourceProperties.class)
 *                                  @ConfigurationProperties(prefix = "spring.datasource")
 *                              --> @Conditional(EmbeddedDatabaseCondition.class)
 *                                  @ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
 *                                  new EmbeddedDatabaseBuilder().build();
 *                              --> @Conditional(PooledDataSourceCondition.class)
 *                                  @ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
 *                                  --> @ConditionalOnClass(org.apache.tomcat.jdbc.pool.DataSource.class)
 *                                      @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "org.apache.tomcat.jdbc.pool.DataSource", matchIfMissing = true)
 *                                      DataSourceProperties.initializeDataSourceBuilder().type(org.apache.tomcat.jdbc.pool.DataSource.class).build();
 *                                  --> @ConditionalOnClass(org.apache.commons.dbcp2.BasicDataSource.class)
 *                                      @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "org.apache.commons.dbcp2.BasicDataSource", matchIfMissing = true)
 *                                      DataSourceProperties.initializeDataSourceBuilder().type(org.apache.commons.dbcp2.BasicDataSource.class).build();
 *                                  --> @ConditionalOnClass(com.zaxxer.hikari.HikariDataSource.class)
 *                                      @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.zaxxer.hikari.HikariDataSource", matchIfMissing = true)
 *                                      DataSourceProperties.initializeDataSourceBuilder().type(com.zaxxer.hikari.HikariDataSource.class).build();
 *                                  
 *  DataSourceTransactionManagerAutoConfiguration --> @ConditionalOnClass({ JdbcTemplate.class, PlatformTransactionManager.class }) 
 *                                                --> @EnableConfigurationProperties(DataSourceProperties.class)
 *                                                    @ConfigurationProperties(prefix = "spring.datasource")
 *                                                --> @ConditionalOnSingleCandidate(DataSource.class)
 *                                                    @ConditionalOnMissingBean(PlatformTransactionManager.class)
 *                                                    new DataSourceTransactionManager(this.dataSource);
 *                                                    
 *  TransactionAutoConfiguration --> @AutoConfigureAfter({ DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
 *                               --> @ConditionalOnClass(PlatformTransactionManager.class)
 *                               --> @EnableConfigurationProperties(TransactionProperties.class)
 *                                   @ConfigurationProperties(prefix = "spring.transaction")
 *                               --> @ConditionalOnSingleCandidate(PlatformTransactionManager.class)
 *                                   @ConditionalOnMissingBean(TransactionTemplate.class)
 *                                   new TransactionTemplate(this.transactionManager);
 *                               --> @ConditionalOnBean(PlatformTransactionManager.class)
 *                                   @ConditionalOnMissingBean(AbstractTransactionManagementConfiguration.class)
 *                                   --> @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false", matchIfMissing = false)
 *                                       @EnableTransactionManagement(proxyTargetClass = false)
 *                                   --> @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = true)
 *                                       @EnableTransactionManagement(proxyTargetClass = true)
 *                                       
 *  JdbcTemplateAutoConfiguration --> @AutoConfigureAfter(DataSourceAutoConfiguration.class)
 *                                --> @ConditionalOnClass({ DataSource.class, JdbcTemplate.class }) 
 *                                    @ConditionalOnSingleCandidate(DataSource.class)
 *                                    --> @ConditionalOnMissingBean(JdbcOperations.class)
 *                                        new JdbcTemplate(this.dataSource);
 *                                    --> @ConditionalOnMissingBean(NamedParameterJdbcOperations.class)
 *                                        new NamedParameterJdbcTemplate(this.dataSource);
 *                                        
 *  HibernateJpaAutoConfiguration --> @AutoConfigureAfter({ DataSourceAutoConfiguration.class })
 *                                --> @ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class })
 *                                    @Conditional(HibernateEntityManagerCondition.class)
 *                                --> @EnableConfigurationProperties(JpaProperties.class)
 *                                    @ConfigurationProperties(prefix = "spring.jpa")
 *                                    --> @ConditionalOnMissingBean(PlatformTransactionManager.class)
 *                                        new JpaTransactionManager();
 *                                    --> @ConditionalOnMissingBean({ LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class })
 *                                        EntityManagerFactoryBuilder.dataSource(this.dataSource).packages(getPackagesToScan())
 *                                              .properties(getVendorProperties()).jta(this.jtaTransactionManager != null).build();
 *                                     
 *                                     
 *  RedisAutoConfiguration --> @ConditionalOnClass({ JedisConnection.class, RedisOperations.class, Jedis.class }) 
 *                         --> @EnableConfigurationProperties(RedisProperties.class) 
 *                             @ConfigurationProperties(prefix = "spring.redis")
 *                         --> @ConditionalOnClass(GenericObjectPool.class)
 *                             @ConditionalOnMissingBean(RedisConnectionFactory.class)
 *                             new JedisConnectionFactory(getClusterConfiguration(), jedisPoolConfig());
 *                         --> @ConditionalOnMissingBean(name = "redisTemplate")
 *                             new RedisTemplate<Object, Object>().setConnectionFactory(redisConnectionFactory);
 *                         --> @ConditionalOnMissingBean(StringRedisTemplate.class)
 *                             new StringRedisTemplate().setConnectionFactory(redisConnectionFactory);
 *                             
 *  KafkaAutoConfiguration --> @ConditionalOnClass(KafkaTemplate.class)
 *                         --> @EnableConfigurationProperties(KafkaProperties.class)
 *                             @ConfigurationProperties(prefix = "spring.kafka")
 *                         --> @ConditionalOnMissingBean(ProducerFactory.class)
 *                             new DefaultKafkaProducerFactory<Object, Object>(this.properties.buildProducerProperties());
 *                         --> @ConditionalOnMissingBean(ProducerListener.class)
 *                             new LoggingProducerListener<Object, Object>();
 *                         --> @ConditionalOnMissingBean(KafkaTemplate.class)
 *                             KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(kafkaProducerFactory);
 *                             kafkaTemplate.setProducerListener(kafkaProducerListener);
 *                             kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
 *                         --> @ConditionalOnMissingBean(ConsumerFactory.class)
 *                             kafkaConsumerFactory = new DefaultKafkaConsumerFactory<Object, Object>(this.properties.buildConsumerProperties());
 *                         --> @ConditionalOnClass(EnableKafka.class)
 *                             --> @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
 *                                 factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
 *                                 ConcurrentKafkaListenerContainerFactoryConfigurer.configure(factory, kafkaConsumerFactory);
 *                             --> @ConditionalOnMissingBean(name = KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
 *                                 @EnableKafka
 *                             
 *  MailSenderAutoConfiguration --> @ConditionalOnClass({ MimeMessage.class, MimeType.class }) 
 *                                  @ConditionalOnMissingBean(MailSender.class)
 *                                  @Conditional(MailSenderCondition.class)
 *                              --> @EnableConfigurationProperties(MailProperties.class)
 *                                  @ConfigurationProperties(prefix = "spring.mail")
 *                                  new JavaMailSenderImpl();
 *  
 *  
 *  ServerPropertiesAutoConfiguration --> @ConditionalOnWebApplication
 *                                    --> @EnableConfigurationProperties
 *                                        @ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
 *                                        
 *  HttpEncodingAutoConfiguration --> @ConditionalOnWebApplication
 *                                    @ConditionalOnClass(CharacterEncodingFilter.class)
 *                                    @ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)
 *                                --> @EnableConfigurationProperties(HttpEncodingProperties.class)
 *                                    @ConfigurationProperties(prefix = "spring.http.encoding")
 *                                --> @ConditionalOnMissingBean(CharacterEncodingFilter.class)
 *                                    filter = new OrderedCharacterEncodingFilter();
 *                                    filter.setEncoding(this.properties.getCharset().name());
 *                                    filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
 *                                    filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
 *                                
 *  EmbeddedServletContainerAutoConfiguration --> @ConditionalOnWebApplication
 *                                            --> @ConditionalOnClass({ Servlet.class, Tomcat.class })
 *                                                @ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class)
 *                                                new TomcatEmbeddedServletContainerFactory();
 *                                            --> @ConditionalOnClass({ Servlet.class, Server.class, Loader.class, WebAppContext.class })
 *                                                @ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class)
 *                                                new JettyEmbeddedServletContainerFactory();
 *                                            --> @ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
 *                                                @ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class)
 *                                                new UndertowEmbeddedServletContainerFactory();
 *                                            --> BeanPostProcessorsRegistrar.class
 *                                            
 *  DispatcherServletAutoConfiguration --> @AutoConfigureAfter(EmbeddedServletContainerAutoConfiguration.class)
 *                                     --> @ConditionalOnWebApplication
 *                                         @ConditionalOnClass(DispatcherServlet.class)
 *                                     --> @EnableConfigurationProperties(WebMvcProperties.class)
 *                                         @ConfigurationProperties(prefix = "spring.mvc")
 *                                         --> @Conditional(DefaultDispatcherServletCondition.class)
 *                                             @ConditionalOnClass(ServletRegistration.class)
 *                                             dispatcherServlet = new DispatcherServlet();
 *                                             @ConditionalOnBean(MultipartResolver.class)
 *                                             // Detect if the user has created a MultipartResolver but named it incorrectly 
 *                                         --> @Conditional(DispatcherServletRegistrationCondition.class)
 *                                             @ConditionalOnClass(ServletRegistration.class)
 *                                             ServletRegistrationBean dispatcherServletRegistration = new ServletRegistrationBean(
 *                                                 dispatcherServlet, this.serverProperties.getServletMapping());
 *                                             dispatcherServletRegistration.setName("dispatcherServlet");
 *                                             dispatcherServletRegistration.setLoadOnStartup(this.webMvcProperties.getServlet().getLoadOnStartup());
 *                                             if (this.multipartConfig != null) {
 *                                                 dispatcherServletRegistration.setMultipartConfig(this.multipartConfig);
 *                                             }
 *                                     
 *  ErrorMvcAutoConfiguration --> @AutoConfigureBefore(WebMvcAutoConfiguration.class)
 *                            --> @ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
 *                            --> @EnableConfigurationProperties(ResourceProperties.class)
 *                                @ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
 *                                --> @ConditionalOnMissingBean(ErrorAttributes.class)
 *                                    new DefaultErrorAttributes();
 *                                --> @ConditionalOnMissingBean(ErrorController.class)
 *                                    new BasicErrorController(errorAttributes, this.serverProperties.getError(), this.errorViewResolvers);
 *                                --> @ConditionalOnBean(DispatcherServlet.class)
 *                                    @ConditionalOnMissingBean(DefaultErrorViewResolver.class)
 *                                    new DefaultErrorViewResolver(this.applicationContext, this.resourceProperties);
 *                            
 *  WebMvcAutoConfiguration --> @AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, ValidationAutoConfiguration.class })
 *                          --> @ConditionalOnWebApplication
 *                              @ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class })
 *                              @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
 *                          --> @ConfigurationProperties(prefix = "spring.mvc")
 *                              @ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
 *                          --> @ConditionalOnMissingBean(InternalResourceViewResolver.class)
 *                              resolver = new InternalResourceViewResolver();
 *                              resolver.setPrefix(this.mvcProperties.getView().getPrefix());
 *                              resolver.setSuffix(this.mvcProperties.getView().getSuffix());
 *                          --> @ConditionalOnBean(View.class)
 *                              @ConditionalOnMissingBean(BeanNameViewResolver.class)
 *                              new BeanNameViewResolver();
 *                          --> @ConditionalOnBean(ViewResolver.class)
 *                              @ConditionalOnMissingBean(name = "viewResolver", value = ContentNegotiatingViewResolver.class)
 *                              resolver = new ContentNegotiatingViewResolver();
 *                              resolver.setContentNegotiationManager(beanFactory.getBean(ContentNegotiationManager.class));
 *                          --> @ConditionalOnProperty(prefix = "spring.mvc", name = "date-format")
 *                              new DateFormatter(this.mvcProperties.getDateFormat());
 *                          
 *                          --> handlerMapping = new RequestMappingHandlerMapping();
 *                              handlerMapping.setInterceptors(getInterceptors());
 *                          --> mapping = new BeanNameUrlHandlerMapping();
 *                              mapping.setInterceptors(getInterceptors());
 *                          --> adapter = new RequestMappingHandlerAdapter();
 *                              adapter.setMessageConverters(getMessageConverters());
 *                          --> conversionService = new DefaultFormattingConversionService();
 *                              addFormatters(conversionService);
 *                          --> new HttpRequestHandlerAdapter();
 *                          --> new SimpleControllerHandlerAdapter();
 *                          
 *  MultipartAutoConfiguration --> @ConditionalOnClass({ Servlet.class, StandardServletMultipartResolver.class, MultipartConfigElement.class })
 *                                 @ConditionalOnProperty(prefix = "spring.http.multipart", name = "enabled", matchIfMissing = true)
 *                             --> @EnableConfigurationProperties(MultipartProperties.class)
 *                             --> @ConfigurationProperties(prefix = "spring.http.multipart", ignoreUnknownFields = false)
 *                             --> @ConditionalOnMissingBean(MultipartResolver.class)
 *                                 multipartResolver = new StandardServletMultipartResolver();
 *                                 
 *  JacksonAutoConfiguration --> @ConditionalOnClass(ObjectMapper.class)
 *  HttpMessageConvertersAutoConfiguration --> @AutoConfigureAfter({ GsonAutoConfiguration.class, JacksonAutoConfiguration.class })
 *                                         --> @ConditionalOnClass(HttpMessageConverter.class)
 *                                         --> @ConditionalOnMissingBean(HttpMessageConverters.class)
 *                                             new HttpMessageConverters(this.converters);
 *                                         --> @ConditionalOnClass(StringHttpMessageConverter.class)
 *                                             @ConfigurationProperties(prefix = "spring.http.encoding")
 *                                             @ConditionalOnMissingBean(StringHttpMessageConverter.class)
 *                                             new StringHttpMessageConverter(this.encodingProperties.getCharset());
 *                                    
 *  
 * @author       zq
 * @date         2017年10月17日  下午4:10:06
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@RestController
@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("com.asiainfo.springboot.application")
public class Application {
	
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
