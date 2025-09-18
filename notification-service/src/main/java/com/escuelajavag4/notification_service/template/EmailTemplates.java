package com.escuelajavag4.notification_service.template;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EmailTemplates {
    public static String reservaConfirmada(BigDecimal amount, String status) {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", Locale.forLanguageTag("es-PE"));
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

        String fechaFormateada = now.format(formatterFecha);
        String horaFormateada = now.format(formatterHora);

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
                      border-radius: 10px;
                      overflow: hidden;
                      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                    }
                    .header {
                      background-color: #4CAF50;
                      color: #ffffff;
                      text-align: center;
                      padding: 20px;
                    }
                    .header h1 {
                      margin: 0;
                      font-size: 22px;
                    }
                    .content {
                      padding: 25px;
                      color: #333333;
                      line-height: 1.6;
                      font-size: 15px;
                    }
                    .details {
                      background-color: #f9f9f9;
                      padding: 15px;
                      border-radius: 8px;
                      margin: 20px 0;
                    }
                    .details p {
                      margin: 8px 0;
                    }
                    .footer {
                      background-color: #fafafa;
                      text-align: center;
                      font-size: 12px;
                      color: #888888;
                      padding: 15px;
                      border-top: 1px solid #eeeeee;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <h1>✅ Pedido Confirmado</h1>
                    </div>
                    <div class="content">
                      <p>¡Tu pedido ha sido confirmado con éxito!</p>
                      <div class="details">
                        <p><strong>📅 Fecha:</strong> %s</p>
                        <p><strong>🕒 Hora:</strong> %s</p>
                        <p><strong>💵 Monto:</strong> %s</p>
                        <p><strong>📦 Estado:</strong> %s</p>
                      </div>
                      <p>Tu pedido está listo para recoger en tienda.</p>
                      <p>Gracias por tu compra 💚</p>
                    </div>
                    <div class="footer">
                      <p>Si tienes alguna pregunta, contáctanos en <a href="mailto:contacto@tuservidor.com">contacto@tuservidor.com</a></p>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(fechaFormateada, horaFormateada, amount, status);
    }
}
