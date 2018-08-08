# HttpServlet代码

```java
public void service(ServletRequest req, ServletResponse res) throws ServletException,IOException {
    if(req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        this.service(request, response);
    } else {
        throw new ServletException("non-HTTP request or response");
    }
}

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        long lastModified;
        if(method.equals("GET")) {
            lastModified = this.getLastModified(req);
            if(lastModified == -1L) {
                this.doGet(req, resp);
            } else {
                long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                if(ifModifiedSince < lastModified) {
                    this.maybeSetLastModified(resp, lastModified);
                    this.doGet(req, resp);
                } else {
                    resp.setStatus(304);
                }
            }
        } else if(method.equals("HEAD")) {
            lastModified = this.getLastModified(req);
            this.maybeSetLastModified(resp, lastModified);
            this.doHead(req, resp);
        } else if(method.equals("POST")) {
            this.doPost(req, resp);
        } else if(method.equals("PUT")) {
            this.doPut(req, resp);
        } else if(method.equals("DELETE")) {
            this.doDelete(req, resp);
        } else if(method.equals("OPTIONS")) {
            this.doOptions(req, resp);
        } else if(method.equals("TRACE")) {
            this.doTrace(req, resp);
        } else {
            String errMsg = lStrings.getString("http.method_not_implemented");
            Object[] errArgs = new Object[]{method};
            errMsg = MessageFormat.format(errMsg, errArgs);
            resp.sendError(501, errMsg);
        }

    }
```

* 开发者只需要实现相应的doXXX方法即可