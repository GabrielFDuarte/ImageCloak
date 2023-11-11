package br.com.imagecloak.imagecloak.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
        String imageData = request.getParameter("imageData");
        String hiddenData = request.getParameter("hiddenData");
        String password = request.getParameter("password");

        // Realiza validações, como verificar se os parâmetros não estão vazios, e validar a senha, se necessário
        String imagemComEsteganografia = realizarEsteganografia(imageData, hiddenData, password);

        response.setContentType("image/png");

        // Converte a imagem esteganografada em bytes e envie como resposta
        byte[] imageBytes = Base64.getDecoder().decode(imagemComEsteganografia);
        response.getOutputStream().write(imageBytes);
    }

    private String realizarEsteganografia(String imageData, String hiddenData, String password) throws IOException {
        try {
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

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagem, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
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
