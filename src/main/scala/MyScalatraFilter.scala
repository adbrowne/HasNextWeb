package com.example

import org.scalatra._
import java.net.URL
import scalate.ScalateSupport

class MyScalatraFilter extends ScalatraFilter with ScalateSupport {
  
  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }
  
  get("/blah") {
    halt(401, "Go away!")
  }

  get("/guess/*"){
    "You missed!"
  }

  get("/guess/:who") {
    params("who") match {
      case "adbrowne" => "You got me!"
      case _ => pass()
    }
  }

  get("/echo") {
    request
  }

  get("/isAjax") {
    request.isAjax
  }

  notFound {
    // If no route matches, then try to render a Scaml template
    val templateBase = requestPath match {
      case s if s.endsWith("/") => s + "index"
      case s => s
    }
    val templatePath = "/WEB-INF/scalate/templates/" + templateBase + ".scaml"
    servletContext.getResource(templatePath) match {
      case url: URL => 
        contentType = "text/html"
        templateEngine.layout(templatePath)
      case _ => 
        filterChain.doFilter(request, response)
    } 
  }
}
