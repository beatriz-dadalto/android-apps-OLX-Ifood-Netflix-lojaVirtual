package com.br.ecommerce.model;

public enum StatusPedido {
    PENDENTE, // 1
    APROVADO, // 2
    CANCELADO; // 3

    public static String getStatus(StatusPedido status) {
        String statusPedido;
        switch (status) {
            case PENDENTE:
                statusPedido = "Pedente";
                break;
            case APROVADO:
                statusPedido = "Aprovado";
                break;
            default:
                statusPedido = "Cancelado";
                break;
        }
        return statusPedido;
    }

}
