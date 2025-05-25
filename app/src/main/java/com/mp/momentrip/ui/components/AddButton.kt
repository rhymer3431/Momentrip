import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button (
        onClick = onClick,
        modifier = modifier
            .width(140.dp)
            .height(31.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF3F1F1),
            contentColor = Color(0xFF656363)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp
        ),
        contentPadding = PaddingValues(0.dp) // 텍스트가 정확히 중앙 오도록
    ) {
        Text(
            text = "추가",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.01.em,
            lineHeight = 20.sp
        )
    }
}
@Preview(showBackground = true, widthDp = 200, heightDp = 60)
@Composable
fun AddButtonPreview() {
    Surface {
        AddButton(
            onClick = {}
        )
    }
}
