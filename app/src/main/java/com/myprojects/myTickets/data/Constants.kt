package com.myprojects.myTickets.data

object Constants{
    const val PROMPT = ("Genera una respuesta en formato JSON válido."+
            "La estructura del json debe ser la siguiente:"+
            "Estos son los datos: " +
            "restaurante (si aparece en el ticket la palabra proforma, esta no puede ser el nombre del restaurante," +
            "cif (Si el cif contiene el caracter - de separacion, eliminalo." +
            "Ten en cuenta que puede haber un espacio blanco de separacion entre numeros y letra," +
            "en tal caso, une la letra a los numeros, la letra debe estar en mayúscula." +
            "Si se trata de un NIF, tambien guardalo en el cif)." +
            "En concreto, estos son los tipos de CIF soportados en españa:" +
            "Consiste en 9 caracteres: 1 letra, 7 números, y 1 carácter de control (letra o número):"+
            "[Letra inicial][7 dígitos][Dígito/letra de control]."+
            "NIF: [8 dígitos][1 letra de control]."+
            "fecha, hora,items(recuerda usar el termino item para" +
            "el nombre de los productos) y sus precios (precioUnidad," +
            "precioTotal), precioSinIva, iva (porcentaje), precioConIva." +
            "A la hora de poner los precios de los productos, debes tener en cuenta que pueden existir" +
            "descuentos en los mismos, ya sea indicado con la palabra descuento, o bien con el signo de"+
            "menos '-', seguido de la cantidad que se descuenta del producto, si esto sucede, asignar "+
            "a PrecioUnidad el resultado de restar el precio original del producto menos el descuento"+
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
            "un total de X euros, incluyendo un IVA del 10.00%. El precio sin IVA se calcula dividiendo " +
            "el precio con IVA entre 1.1. Devuelve el resultado como un número decimal con dos lugares decimales. " +
            "Por ejemplo, si el precio con IVA es 11.00 euros, el precio sin IVA sería 10.00 euros."+
            "Se supone que todos los tickets son del sector de la restauracion, es decir de un 10.00% en España."+
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
            "Si no aparece restaurante, poner en el campo: DESCONOCIDO." +
            "Si no aparece cif, poner en el campo: DESCONOCIDO." +
            "Si no aparece fecha, poner en el campo: DESCONOCIDA." +
            "Si no aparece hora, poner en el campo: DESCONOCIDA." +
            "Tenemos el iva(en porcentaje)." +
            "El formato de la fecha deber ser del tipo: dd/MM/YYYY"+
            "El formato de la hora debe ser del tipo: HH:MM, aunque estén indicados los segundos"+
            "El formato de precioUnidad precioTotal, precioConIva, precioSinIva debe ser siempre " +
            "con 2 decimales, ejemplo: 5.00, 7.56, 4.40."+
            "Utiliza el esquema para estructurar el JSON, pero asegúrate de que los valores sean " +
            "coherentes con los ejemplos proporcionados." +
            "{\n" +
            "  \"restaurante\": Nombre del restaurante (ejemplo: \"LA BUENA MESA\"),\n" +
            "  \"cif\": Código fiscal del restaurante (ejemplo: \"B12345678\"),\n" +
            "  \"fecha\": Fecha del ticket en formato dd/MM/YYYY (ejemplo: \"19/11/2024\"),\n" +
            "  \"hora\": Hora del ticket en formato HH:MM (ejemplo: \"13:45\"),\n" +
            "  \"items\": [\n" +
            "    {\n" +
            "      \"item\": Nombre del producto (ejemplo: \"Paella de mariscos\"),\n" +
            "      \"cantidad\": Cantidad comprada en texto (ejemplo: \"2\"),\n" +
            "      \"precioUnidad\": Precio por unidad en texto (ejemplo: \"15.00\"),\n" +
            "      \"precioFinal\": Precio total del producto en texto (ejemplo: \"30.00\")\n" +
            "    }\n" +
            "  ],\n" +
            "  \"precioSinIva\": Precio total del ticket sin IVA (ejemplo: \"50.00\"),\n" +
            "  \"iva\": Valor del IVA aplicado al ticket (ejemplo: \"10.00\"),\n" +
            "  \"precioConIva\": Precio total del ticket con IVA incluido (ejemplo: \"60.00\")\n" +
            "}\n" +
            "\n"
            )
}
