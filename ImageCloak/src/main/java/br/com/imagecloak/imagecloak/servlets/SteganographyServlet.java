package br.com.imagecloak.imagecloak.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabriel Duarte
 */
public class SteganographyServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recupera os parâmetros do formulário
        String imageData = request.getParameter("imageData");
        String hiddenData = request.getParameter("hiddenData");
        String password = request.getParameter("password");

        // Realiza validações, como verificar se os parâmetros não estão vazios, e validar a senha, se necessário
        // Chama o método para realizar a esteganografia
        String imagemComEsteganografia = realizarEsteganografia(imageData, hiddenData, password);

        // Envia a imagem finalizada com esteganografia como resposta
        response.setContentType("image/png"); // Defina o tipo de conteúdo como uma imagem PNG
        response.getOutputStream().write(Base64.getDecoder().decode(imagemComEsteganografia));

//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet SteganographyServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet SteganographyServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
    }

    private String realizarEsteganografia(String imageData, String hiddenData, String password) throws IOException {
        // Carrega a imagem de hospedeiro
        //BufferedImage imagem = ImageIO.read(new File("imagem.png"));
        BufferedImage imagem = ImageIO.read(new File(imageData));

        // Converte o texto a ser ocultado em uma sequência de bits
        byte[] bytes = hiddenData.getBytes();

        // Oculta os bits nos pixels da imagem
        int byteIndex = 0;
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                int pixel = imagem.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                if (byteIndex < bytes.length) {
                    red = (red & 0xFE) | ((bytes[byteIndex] >> 7) & 0x01);
                    green = (green & 0xFE) | ((bytes[byteIndex] >> 6) & 0x01);
                    blue = (blue & 0xFC) | ((bytes[byteIndex] >> 5) & 0x03);
                    byteIndex++;
                }

                pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                imagem.setRGB(x, y, pixel);
            }
        }
        
        // Salva a imagem com os dados já ocultos
        ImageIO.write(imagem, "png", new File("imagem_com_esteganografia.png"));

        return imagemComEsteganografia;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
