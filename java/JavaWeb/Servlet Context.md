# Servlet Context

## Configuration methods 

The following methods are added to ServletContext since Servlet 3.0 to enable programmatic definition of servlets, filters and the url pattern that they map to.  

* Programmatically adding and configuring Servlets 
* Programmatically adding and configuring Filters 
* Programmatically adding and configuring Listeners 



## Error Pages 

* To allow developers to customize the appearance of content returned to a Web client when a servlet generates an error 
*  the deployment descriptor defines a list of error page descriptions 
* 当Servelt或者filter发送错误，并调用sendError方法时
* If the sendError method is called on the response, the container consults the list of error page declarations for the Web application that use the status-code syntax and attempts a match. If there is a match, the container returns the resource as indicated by the location entry
* The error page mechanism described does not intervene when errors occur when invoked using the RequestDispatcher or filter.doFilter method. In this way, a filter or servlet using the RequestDispatcher has the opportunity to handle errors generated 
* If a servlet generates an error that is not handled by the error page mechanism as described above, the container must ensure to send a response with status 500 
* The default servlet and container will use the sendError method to send 4xx and 5xx status responses, so that the error mechanism may be invoked. The default servlet and container will use the setStatus method for 2xx and 3xx responses and will not invoke the error page mechanism 