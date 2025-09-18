package com.escuelajavag4.notification_service.template;

public class EmailTemplates {
    public static String reservaConfirmada(){
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Reserva Confirmada</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      background-color: #f4f4f4;
                      margin: 0;
                      padding: 0;
                    }
                    .container {
                      max-width: 600px;
                      margin: 20px auto;
                      background-color: #ffffff;
                      padding: 20px;
                      border-radius: 8px;
                      box-shadow: 0 0 10px rgba(0,0,0,0.1);
                    }
                    .header {
                      text-align: center;
                      padding-bottom: 20px;
                      border-bottom: 1px solid #eeeeee;
                    }
                    .header h1 {
                      margin: 0;
                      color: #4CAF50;
                    }
                    .content {
                      padding: 20px 0;
                      line-height: 1.6;
                      color: #333333;
                    }
                    .content p {
                      margin: 10px 0;
                    }
                    .button {
                      display: inline-block;
                      padding: 12px 20px;
                      margin-top: 20px;
                      background-color: #4CAF50;
                      color: #ffffff !important;
                      text-decoration: none;
                      border-radius: 5px;
                    }
                    .footer {
                      text-align: center;
                      font-size: 12px;
                      color: #888888;
                      padding-top: 20px;
                      border-top: 1px solid #eeeeee;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <h1>Reserva Confirmada</h1>
                    </div>
                    <div class="content">
                      <p>Hola <strong>Juan Pérez</strong>,</p>
                      <p>¡Tu reserva ha sido confirmada con éxito! A continuación, los detalles de tu reserva:</p>
                      <p><strong>Fecha:</strong> 20 de septiembre, 2025</p>
                      <p><strong>Hora:</strong> 18:30</p>
                      <p><strong>Servicio:</strong> Cena para 2 personas</p>
                      <p><strong>Ubicación:</strong> Restaurante El Buen Sabor</p>
                      <p>Si necesitas modificar o cancelar tu reserva, haz clic en el siguiente botón:</p>
                      <a href="https://tuservidor.com/reservas" class="button">Administrar Reserva</a>
                    </div>
                    <div class="footer">
                      <p>Gracias por elegirnos. Si tienes alguna pregunta, contáctanos en contacto@tuservidor.com</p>
                    </div>
                  </div>
                </body>
                </html>
                """;
    }
}
