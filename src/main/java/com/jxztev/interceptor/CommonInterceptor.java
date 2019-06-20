/**
 * 通用拦截器示例代码，可根据需要调整、重写、删除等
 */
package com.jxztev.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author winnerlbm
 * @date 2018年5月18日
 * @desc 关于拦截器原理说明：</br>
1、preHandle(HttpServletRequest request, HttpServletResponse response, Object handle)方法，该方法在请求处理之前进行调用。SpringMVC 中的 Interceptor 是链式调用的，在一个应用中或者说是在一个请求中可以同时存在多个 Interceptor 。每个 Interceptor 的调用会依据它的声明顺序依次执行，而且最先执行的都是 Interceptor 中的 preHandle 方法，所以可以在这个方法中进行一些前置初始化操作或者是对当前请求做一个预处理，也可以在这个方法中进行一些判断来决定请求是否要继续进行下去。该方法的返回值是布尔值 Boolean 类型的，当它返回为 false 时，表示请求结束，后续的 Interceptor 和 Controller 都不会再执行；当返回值为 true 时，就会继续调用下一个 Interceptor 的 preHandle 方法，如果已经是最后一个 Interceptor 的时候，就会是调用当前请求的 Controller 中的方法。</br>
2、postHandle(HttpServletRequest request, HttpServletResponse response, Object handle, ModelAndView modelAndView)方法，通过 preHandle 方法的解释咱们知道这个方法包括后面要说到的 afterCompletion 方法都只能在当前所属的 Interceptor 的 preHandle 方法的返回值为 true 的时候，才能被调用。postHandle 方法在当前请求进行处理之后，也就是在 Controller 中的方法调用之后执行，但是它会在 DispatcherServlet 进行视图返回渲染之前被调用，所以咱们可以在这个方法中对 Controller 处理之后的 ModelAndView 对象进行操作。postHandle 方法被调用的方向跟 preHandle 是相反的，也就是说，先声明的 Interceptor 的 postHandle 方法反而会后执行。这和 Struts2 里面的 Interceptor 的执行过程有点类型，Struts2 里面的 Interceptor 的执行过程也是链式的，只是在 Struts2 里面需要手动调用 ActionInvocation 的 invoke 方法来触发对下一个 Interceptor 或者是 action 的调用，然后每一个 Interceptor 中在 invoke 方法调用之前的内容都是按照声明顺序执行的，而 invoke 方法之后的内容就是反向的。</br>
3、afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handle, Exception ex)方法，也是需要当前对应的 Interceptor 的 preHandle 方法的返回值为 true 时才会执行。因此，该方法将在整个请求结束之后，也就是在 DispatcherServlet 渲染了对应的视图之后执行，这个方法的主要作用是用于进行资源清理的工作。</br>
PS:以上3个方法，可以根据需求只重写其中任意一个或多个方法。</br>
4、afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)方法，并发处理开始之后执行。</br>
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {
	@Override  
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {  
		System.out.println("===========拦截器 preHandle");  
		return true;  
	}  
	@Override  
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {  
		System.out.println("===========拦截器 postHandle");  
	}  
	@Override  
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {  
		System.out.println("===========拦截器 afterCompletion");  
	}  

	/** @desc 该方法非必须重写，可以直接去掉。
	 */
	@Override
	public void afterConcurrentHandlingStarted(
			HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
		System.out.println("HandlerInterceptor, afterConcurrentHandlingStarted......");
	}
}
