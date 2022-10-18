package com.br.ecommerce.helper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.text.DateFormat.getDateTimeInstance;

public class GetMask {

    public static String getDate(long dataPedido, int tipo) {

        Locale locale = new Locale("pt", "BR");
        String fuso = "America/Sao_Paulo";

        SimpleDateFormat diaSdf = new SimpleDateFormat("dd", locale);
        diaSdf.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat mesSdf = new SimpleDateFormat("MM", locale);
        mesSdf.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat anoSdf = new SimpleDateFormat("yyyy", locale);
        anoSdf.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat horaSdf = new SimpleDateFormat("HH", locale);
        horaSdf.setTimeZone(TimeZone.getTimeZone(fuso));

        SimpleDateFormat minutoSdf = new SimpleDateFormat("mm", locale);
        minutoSdf.setTimeZone(TimeZone.getTimeZone(fuso));

        DateFormat dateFormat = getDateTimeInstance();
        Date netDate = (new Date(dataPedido));
        dateFormat.format(netDate);

        String dia = diaSdf.format(netDate);
        String mes = mesSdf.format(netDate);
        String ano = anoSdf.format(netDate);

        String hora = horaSdf.format(netDate);
        String minuto = minutoSdf.format(netDate);

        String time;
        if(tipo == 1){
            time = dia + "/" + mes + "/" + ano;
        }else {
            time = dia + "/" + mes + "/" + ano + " " + hora + ":" + minuto;
        }
        return time;
    }

    public static String getValor(double valor) {
        NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(
                new Locale("pt", "BR")));
        return nf.format(valor);
    }

}
