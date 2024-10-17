package com.myprojects.myTickets.data

object Constants{
    const val Prompt = "genera un archivo json. La respuesta de la api unicamente debe devolver" +
            "en la respuesta el archivo JSON, con la siguiente estructura:" +
            " Estos son los datos: restaurante," +
            "cif (Si el cif contiene el caracter - de separacion, eliminalo." +
            "Ten en cuenta que puede haber un espacio blanco de separacion entre numeros y letra," +
            "en tal caso, une la latra a los numeoros." +
            "Si se trata de un NIF, tambien guardalo en el cif) fecha,hora, " +
            "items(recuerda usar el termino item para" +
            "el nombre de los productos) y sus precios (precioUnidad," +
            "precioTotal), precioSinIva, iva (porcentaje), precioConIva. " +
            "Los valores numericos no deben tener ningun simbolo detras." +
            "Asegurate de que solo se incluyan estos datos, hazlo en español. En" +
            "caso de haber caracteres extraños, eliminalos. Genera el json con todos los items," +
            "Los items deben contener los elementos: item, cantidad, precioUnidad, precioFinal"+
            "Además, debes tener en cuenta que puede haber varias unidades de cada item, " +
            "y eso puede afectar al precio final de cada item. Si se especifica las unidades del" +
            "producto, debes indicar la cantidad (entero sin decimales)," +
            "el precioUnidad, y el precioFinal de la suma" +
            "de la cantidad de items.Recuerda usar item para el nombre de los productos"+
            "Los decimales deben mararcarse con el simbolo . no el simbolo ,"+
            "Recuerda que precioUnidad y precioFinal deben tener obligatoriamente 2 decimales" +
            "Los precios deben ser de tipo double y no deben ir entre comillas nunca"+
            "Ten en cuenta que los nombres de los atributos" +
            "no pueden contener espacios, deben ir unidos por el caracter _"+
            "si no aparece restaurante, fecha, cif, hora, poner en el campo: campo no especificado" +
            "Ten en cuenta que el precio total corresponde a precio_con_iva"+
            "Tenemos el iva(en porcentaje)." +
            "Por ultimo (precio_sin_iva = precio_con_iva / (1 + iva)). Si iva es 10%, entonces" +
            "precio_sin_iva = precio_con_iva / (1 + 0.1)"+
            "El formato de la fecha deber ser del tipo: dd/MM/YYYY"
}
