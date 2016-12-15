package org.collector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class DataCollector extends AbstractHandler {
    private final DataWriter dataWriter = new DataWriter();
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("OK");

        synchronized (dataWriter) {
            String str = request.getParameter(Const.GET_DATA_PARAM);
            String userAgent = request.getParameter(Const.GET_USER_AGENT_PARAM);
            if (str != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DATE_TIME_FORMAT);
                String date = dateFormat.format(new Date());
                String line = date + "," + str;
                if (userAgent != null) {
                    line = line + ",\"" + request.getHeader("User-Agent") + "\"";
                }
                dataWriter.writeToFile(line);
                System.out.println("Get data: " + line);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(Const.PORT);
        server.setHandler(new DataCollector());
        server.start();
        System.out.println("DataCollector server " + Const.VERSION);
        System.out.println("Visit to http://" + Inet4Address.getLocalHost().getHostAddress() + ":" + Const.PORT + "/?data=[there are your data]");
        server.join();

    }


}
