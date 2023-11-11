<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ImageCloak - Esteganografia</title>
    </head>
    <body>
        <h1>ImageCloak - Ocultação</h1>

        <form action="steganography" method="post" enctype="multipart/form-data">
            <label for="imageData">Escolha a imagem hospedeira:</label>
            <input type="file" name="imageData" accept="image/*" required><br>

            <label for="hiddenData">Digite os dados que serão ocultados:</label>
            <textarea name="hiddenData" rows="4" cols="50" required></textarea><br>

            <input type="submit" value="Ocultar">
        </form>
    </body>
</html>
