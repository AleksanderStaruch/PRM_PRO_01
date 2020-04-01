package com.example.prm_pro_01

data class Debtor(var name: String, var debt: Double) {

    override fun toString(): String {
        return "$name  $debt"
    }
}