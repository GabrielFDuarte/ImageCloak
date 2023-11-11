<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ImageCloak - Esteganografia</title>
    </head>
    <body>
        <h1>ImageCloak - Recuperação</h1>

        <form action="recover" method="post">
            <label for="imagemEsteganografada">Escolha a imagem hospedeira:</label>
            <input type="file" name="imagemEsteganografada" accept="image/*" required><br>

            <input type="submit" value="Recuperar">
        </form>
    </body>
</html>
