package com.example.lab6_23520536_21521202.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lab6_23520536_21521202.R
import com.example.lab6_23520536_21521202.auth.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Ánh xạ giao diện
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_user_avatar)
        val tvName = view.findViewById<TextView>(R.id.tv_user_name)
        val tvEmail = view.findViewById<TextView>(R.id.tv_user_email)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btn_dang_xuat)

        // ========================================================
        // TỰ ĐỘNG KÉO THÔNG TIN TỪ TÀI KHOẢN GOOGLE
        // ========================================================
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // 1. Cập nhật Tên và Email
            tvName.text = currentUser.displayName ?: "Người dùng vô danh"
            tvEmail.text = currentUser.email ?: "Không có email"

            // 2. Tải ảnh đại diện bằng Glide và bo tròn
            val photoUrl = currentUser.photoUrl
            if (photoUrl != null) {
                // Xóa cái màu xanh mặc định đi để hiện ảnh thực tế
                ivAvatar.imageTintList = null

                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop() // Cắt ảnh thành hình tròn
                    .into(ivAvatar)
            }
        } else {
            // BỔ SUNG: Xử lý trường hợp chưa đăng nhập (Firebase mất session trên máy ảo)
            tvName.text = "Chưa đăng nhập"
            tvEmail.text = "Vui lòng đăng xuất và đăng nhập lại"
        }

        // ========================================================
        // XỬ LÝ NÚT ĐĂNG XUẤT
        // ========================================================
        btnLogout.setOnClickListener {
            // Đăng xuất khỏi hệ thống Firebase
            mAuth.signOut()

            // Chuyển trang về Login và xóa trắng lịch sử lướt app
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(requireContext(), "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show()
        }
    }
}