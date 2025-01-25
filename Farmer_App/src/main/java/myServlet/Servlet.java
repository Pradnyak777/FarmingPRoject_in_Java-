package myServlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;



@WebServlet("/Servlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class Servlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Get upload directory path
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Collect form data
        String farmerName = request.getParameter("farmerName");
        String farmerContact = request.getParameter("farmerContact");
        String treeSpecies = request.getParameter("treeSpecies");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        // Handle file upload
        Part fieldPhoto = request.getPart("fieldPhoto");
        String dataset = extractFileName(fieldPhoto);
        if (dataset != null && !dataset.isEmpty()) {
            String filePath = uploadPath + File.separator + dataset;
            fieldPhoto.write(filePath);
        }

        // Respond with submitted data
        out.println("<h1>Form Submitted Successfully!</h1>");
        out.println("<p>Farmer Name: " + farmerName + "</p>");
        out.println("<p>Farmer Contact: " + farmerContact + "</p>");
        out.println("<p>Tree Species: " + treeSpecies + "</p>");
        out.println("<p>Location: " + latitude + ", " + longitude + "</p>");
        out.println("<p>Field Photo: " + dataset + "</p>");
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String content : contentDisp.split(";")) {
            if (content.trim().startsWith("dataset")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}