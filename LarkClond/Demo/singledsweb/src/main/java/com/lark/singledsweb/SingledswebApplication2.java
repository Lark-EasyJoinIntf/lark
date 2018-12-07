package com.lark.singledsweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

public class SingledswebApplication2 {

	public static void main(String[] args) {
		SpringApplication.run(SingledswebApplication2.class, args);
	}

	/*@Aspect
	@Componentpublic
	class WebExceptionAspect {
		private static final Logger logger = LoggerFactory.getLogger(WebExceptionAspect.class);//凡是注解了RequestMapping的方法都被拦截

		@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
		private void webPointcut() {
		}

		*//**
		 * 拦截web层异常，记录异常日志，并返回友好信息到前端 目前只拦截Exception，是否要拦截Error需再做考虑
		 *  @param e   异常对象
		 *//*
		@AfterThrowing(pointcut = "webPointcut()", throwing = "e")
		public void handleThrowing(Exception e) {
			e.printStackTrace();
			logger.error("发现异常！" + e.getMessage());
			logger.error(JSON.toJSONString(e.getStackTrace()));        //这里输入友好性信息
			writeContent("出现异常");
		}

		*//**
		 * 将内容输出到浏览器     *     * @param content     *            输出内容
		 *//*
		private void writeContent(String content) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type", "text/plain;charset=UTF-8");
			response.setHeader("icop-content-type", "exception");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.print(content);
			writer.flush();
			writer.close();
		}
	}


	@Component
	@Aspect
	public class WebExceptionAspect implements ThrowsAdvice {
		public static final Logger logger = LoggerFactory.getLogger(WebExceptionAspect.class);//拦截被GetMapping注解的方法

		@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
		private void webPointcut() {
		}

		@AfterThrowing(pointcut = "webPointcut()", throwing = "e")
		public void afterThrowing(Exception e) throws Throwable {
			logger.debug("exception 来了！");
			if (StringUtils.isNotBlank(e.getMessage())) {
				writeContent(e.getMessage());
			} else {
				writeContent("参数错误！");
			}
		}

		*//**
		 * 将内容输出到浏览器     *     * @param content     *            输出内容
		 *//*
		private void writeContent(String content) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getResponse();
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type", "text/plain;charset=UTF-8");
			response.setHeader("icop-content-type", "exception");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.print((content == null) ? "" : content);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void validate(BindingResult result) {
		if (result.hasFieldErrors()) {
			List<FieldError> errorList = result.getFieldErrors();
			errorList.stream().forEach(item -> Assert.isTrue(false, item.getDefaultMessage()));
		}
	}


	@GetMapping("authorize")
	public void authorize(@Valid AuthorizeIn authorize, BindingResult result) {
		if (result.hasFieldErrors()) {
			List<FieldError> errorList = result.getFieldErrors();            //通过断言抛出参数不合法的异常
			errorList.stream().forEach(item -> Assert.isTrue(false, item.getDefaultMessage()));
		}
	}

	public class AuthorizeIn extends BaseModel {
		@NotBlank(message = "缺少response_type参数")
		private String responseType;
		@NotBlank(message = "缺少client_id参数")
		private String ClientId;
		private String state;
		@NotBlank(message = "缺少redirect_uri参数")
		private String redirectUri;

		public String getResponseType() {
			return responseType;
		}

		public void setResponseType(String responseType) {
			this.responseType = responseType;
		}

		public String getClientId() {
			return ClientId;
		}

		public void setClientId(String clientId) {
			ClientId = clientId;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getRedirectUri() {
			return redirectUri;
		}

		public void setRedirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
		}
	}


	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Mappingpublic
	@interface ApiVersion {
		*//**
		 * 标识版本号     * @return
		 *//*
		int value();
	}

	public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
		// 路径中版本的前缀， 这里用 /v[1-9]/的形式
		/
		private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
		private int apiVersion;

		public ApiVersionCondition(int apiVersion) {
			this.apiVersion = apiVersion;
		}

		@Override
		public ApiVersionCondition combine(ApiVersionCondition other) {
			// 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
			return new ApiVersionCondition(other.getApiVersion());
		}

		@Override
		public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
			Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
			if (m.find()) {
				Integer version = Integer.valueOf(m.group(1));
				if (version >= this.apiVersion) {
					return this;
				}
			}
			return null;
		}

		@Override
		public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
			// 优先匹配最新的版本号
			return other.getApiVersion() - this.apiVersion;
		}

		public int getApiVersion() {
			return apiVersion;
		}
	}

	public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
		@Override
		protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
			ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
			return createCondition(apiVersion);
		}

		@Override
		protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
			ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
			return createCondition(apiVersion);
		}

		private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
			return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
		}
	}

	@SpringBootConfiguration
	public
	class WebConfig extends WebMvcConfigurationSupport {
		@Bean
		public AuthInterceptor interceptor() {
			return new AuthInterceptor();
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new AuthInterceptor());
		}

		@Override
		@Bean
		public RequestMappingHandlerMapping requestMappingHandlerMapping() {
			RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
			handlerMapping.setOrder(0);
			handlerMapping.setInterceptors(getInterceptors());
			return handlerMapping;
		}
	}

	@ApiVersion(1)
	@RequestMapping("{version}/dd")
	public class HelloController {

	}


	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		*//* 1.需要先定义一个convert转换消息的对象；
		2.添加fastjson的配置信息，比如是否要格式化返回的json数据
		 3.在convert中添加配置信息
		 4.将convert添加到converters中         *//*
		//1.定义一个convert转换消息对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		//2.添加fastjson的配置信息，比如：是否要格式化返回json数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		converters.add(fastConverter);
	}
*/


}
