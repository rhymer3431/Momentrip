package com.mp.momentrip.data

data class Schedule (
    val duration : Int,
    val region : String,
    val days : List<Day>
){
    fun printInfo() {
        println("Schedule Duration: $duration days")
        println("Region: $region")
        println("Days:")
        days.forEachIndexed { index, day ->
            println("  Day ${index + 1}:")

            day.timeTable.forEach { timeSlot ->
                println("    Time: ${timeSlot.time}")
                println("    Place: ${timeSlot.place.name} (${timeSlot.place.category})")
                println("    Address: ${timeSlot.place.address}")
                println("    Phone: ${timeSlot.place.phone}")
                println("    Coordinates: (${timeSlot.place.x}, ${timeSlot.place.y})")
                println()
            }
        }
    }
}
