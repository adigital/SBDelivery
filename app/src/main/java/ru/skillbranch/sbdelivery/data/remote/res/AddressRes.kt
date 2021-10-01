package ru.skillbranch.sbdelivery.data.remote.res

data class AddressRes(
    val city: String? = null,
    val street: String? = null,
    val house: String? = null,
)

fun AddressRes.toAddressString(): String? {
    var addressString: String? = null

    if (this.city != null) {
        addressString = "г. ${this.city}"
        if (this.street != null) {
            addressString += ", ул. ${this.street}"
            if (this.house != null) {
                addressString += ", д. ${this.house}"
            }
        }
    }

    return addressString
}

fun AddressRes.isFull() = (this.city != null) && (this.street != null) && (this.house != null)