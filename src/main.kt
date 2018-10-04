import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer

import java.io.IOException
import java.net.InetSocketAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SimpleHttpServer{

    @Throws(Exception::class)
    @JvmStatic

    fun main(args: Array<String>){
        val server =  HttpServer.create(InetSocketAddress(8080),0)
        server.createContext("/", EchoHandler())
        server.createContext("/date", DateHandler())
        server.createContext("/wait", WaitHandler())
        server.executor = null
        server.start()

    }

    internal class EchoHandler : HttpHandler{
        @Throws(IOException::class)
        override fun handle(t:HttpExchange){
            val response = "Hello!"
            val os = t.responseBody
            t.sendResponseHeaders(200,response.length.toLong())
            os.write(response.toByteArray())
            os.close()
        }
    }

    internal class DateHandler : HttpHandler{
        @Throws(IOException::class)
        override fun handle(t: HttpExchange){
            val response = LocalDateTime.now().format((DateTimeFormatter.ISO_DATE_TIME))
            t.sendResponseHeaders(200, response.length.toLong())
            val os = t.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }

    internal  class WaitHandler : HttpHandler{
        @Throws(IOException::class)
        override fun handle(t: HttpExchange){
            val delayTime = t.requestURI.query.toString().split("=")[1]
            Thread.sleep(delayTime.toLong())
            val response = "Sleep for $delayTime ms completed"
            val os = t.responseBody
            t.sendResponseHeaders(200,response.length.toLong())
            os.write(response.toByteArray())
            os.close()
        }
    }
}