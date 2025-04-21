package academy.nouri.s4_mvvm.food_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvm.food_app.utils.FOOD_DB_TABLE

@Entity(tableName = FOOD_DB_TABLE)
data class FoodEntity(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var img: String = ""
)