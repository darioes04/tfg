package com.myprojects.myTickets.data

object Constants{
    const val Prompt = ("Genera una respuesta en formato JSON válido. No especifiques que se trata de un JSON" +
            "Solo necesito el contenido del mismo." +
            "La estructura del json debe ser la siguiente:"+
            "Estos son los datos: restaurante," +
            "cif (Si el cif contiene el caracter - de separacion, eliminalo." +
            "Ten en cuenta que puede haber un espacio blanco de separacion entre numeros y letra," +
            "en tal caso, une la latra a los numeoros." +
            "Si se trata de un NIF, tambien guardalo en el cif) fecha,hora, " +
            "items(recuerda usar el termino item para" +
            "el nombre de los productos) y sus precios (precioUnidad," +
            "precioTotal), precioSinIva, iva (porcentaje), precioConIva." +
            "Los valores numericos no deben tener ningun simbolo detras." +
            "Asegurate de que solo se incluyan estos datos, hazlo en español. En" +
            "caso de haber caracteres extraños, eliminalos. Genera el json con todos los items," +
            "Los items deben contener los elementos: item, cantidad, precioUnidad, precioFinal"+
            "Además, debes tener en cuenta que puede haber varias unidades de cada item, " +
            "y eso puede afectar al precio final de cada item. Si se especifica las unidades del" +
            "producto, debes indicar la cantidad (entero sin decimales)," +
            "el precioUnidad, y el precioFinal de la suma" +
            "de la cantidad de items.Recuerda usar item para el nombre de los productos"+
            "El precioConIva normalmente vendra representado en el ticket como TOTAL o bien el precio" +
            "numérico con una fuente más grande para que se vea claramente."+
            "Calcula el precio sin IVA de un recibo de restaurante español. El recibo adjunto muestra " +
            "un total de X euros, incluyendo un IVA del 10%. El precio sin IVA se calcula dividiendo " +
            "el precio con IVA entre 1.1. Devuelve el resultado como un número decimal con dos lugares decimales. " +
            "Por ejemplo, si el precio con IVA es 11.00 euros, el precio sin IVA sería 10.00 euros."+
            "Se supone que todos los tickets son del sector de la restauracion, es decir de un 10% en España."+
            "Los decimales deben marcarse con el simbolo . no el simbolo ,"+
            "Es importante que compruebes que el precioFinal de cada producto sea el resultado de "+
            "multiplicar cantidad y precioUnidad, si esta operación no se verifica, ajustar los valores" +
            "cantidad o precioUnidad para dar sentido."+
            "Ten en cuenta que el precioConIva debe ser igual a la suma de todos los precioFinal de los productos."+
            "Si no coincide, debes comprobar cuidadosamente todos los precios y ajustarlos para que"+
            "esta cuenta sea correcta."+
            "Recuerda que precioUnidad y precioFinal deben tener obligatoriamente 2 decimales" +
            "Los precios deben ser de tipo double y no deben ir entre comillas nunca"+
            "Ten en cuenta que los nombres de los atributos" +
            "no pueden contener espacios, deben ir unidos por el caracter _"+
            "si no aparece restaurante, fecha, cif, hora, poner en el campo: campo no especificado." +
            "Ten en cuenta que el precio total corresponde a precioConIva"+
            "Tenemos el iva(en porcentaje)." +

            "El formato de la fecha deber ser del tipo: dd/MM/YYYY"+
            "El formato de precioUnidad precioTotal, precioConIva, precioSinIva debe ser siempre " +
            "con 2 decimales, ejemplo: 5.00, 7.56, 4.40."+
            "Aqui te dejo un ejemplo para que tengas en cuanto el formato correcto esperado:" +
            "{\n" +
            "\"restaurante\":\"RESTAURANTE PUERTO RICO\",\n" +
            "\"cif\":\"5418479U\",\n" +
            "\"fecha\":\"24/01/2015\",\n" +
            "\"hora\":\"22:30\",\n" +
            "\"items\":[\n" +
            "{\"item\":\"BARRITA PAN\",\"cantidad\":2,\"precioUnidad\":0.60,\"precioFinal\":1.20},\n" +
            "{\"item\":\"AGUA MINERAL SIN GAS\",\"cantidad\":2,\"precioUnidad\":1.50,\"precioFinal\":3.00},\n" +
            "{\"item\":\"ENTRECOT DE AVILA\",\"cantidad\":1,\"precioUnidad\":8.50,\"precioFinal\":8.50},\n" +
            "{\"item\":\"ARROZ A LA CUBANA\",\"cantidad\":1,\"precioUnidad\":3.25,\"precioFinal\":3.25},\n" +
            "{\"item\":\"1/2 POLLO AJILLO\",\"cantidad\":1,\"precioUnidad\":4.00,\"precioFinal\":4.00},\n" +
            "{\"item\":\"GUARNICION\",\"cantidad\":1,\"precioUnidad\":-1.00,\"precioFinal\":-1.00},\n" +
            "{\"item\":\"GUARNICION ENSALADA\",\"cantidad\":1,\"precioUnidad\":1.00,\"precioFinal\":1.00}\n" +
            "],\n" +
            "\"precioSinIva\":18.14,\n" +
            "\"iva\":10,\n" +
            "\"precioConIva\":19.95\n" +
            "}\n")
}
