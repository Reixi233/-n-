package com.example.lab6_23520536_21521202.fragment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.lab6_23520536_21521202.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchNotif = view.findViewById<MaterialSwitch>(R.id.switch_notifications)
        val btnTestNotif = view.findViewById<MaterialButton>(R.id.btn_test_notification)

        // 1. Xin quyền hiển thị thông báo cho Android 13 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // 2. Tạo Kênh thông báo (Bắt buộc phải có từ Android 8.0)
        createNotificationChannel()

        // 3. Xử lý khi bấm nút Test
        btnTestNotif.setOnClickListener {
            if (switchNotif.isChecked) {
                Toast.makeText(requireContext(), "Đã đặt lịch! Hãy thử ẩn ứng dụng xuống nhé...", Toast.LENGTH_LONG).show()

                // Hẹn giờ 5 giây sau sẽ gọi hàm bắn thông báo
                Handler(Looper.getMainLooper()).postDelayed({
                    sendNotification()
                }, 5000)
            } else {
                Toast.makeText(requireContext(), "Vui lòng bật công tắc nhận thông báo!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MEDICAL_CHANNEL",
                "Lịch Khám Bệnh",
                NotificationManager.IMPORTANCE_HIGH // Đặt HIGH để thông báo rớt xuống màn hình (Heads-up)
            ).apply {
                description = "Kênh thông báo nhắc nhở lịch khám"
            }
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(requireContext(), "MEDICAL_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Icon hệ thống
            .setContentTitle("🔔 Nhắc nhở từ Medical App")
            .setContentText("Đã đến giờ đi khám bệnh! Vui lòng kiểm tra lại lịch trình của bạn.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(1001, builder.build())
            }
        }
    }
}