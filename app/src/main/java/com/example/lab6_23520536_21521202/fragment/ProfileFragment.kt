package com.example.lab6_23520536_21521202.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lab6_23520536_21521202.R
import com.example.lab6_23520536_21521202.auth.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    private lateinit var tvName: TextView
    private lateinit var tvDob: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvBhyt: TextView
    private lateinit var tvAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tv_profile_name)
        tvDob = view.findViewById(R.id.tv_profile_dob)
        tvGender = view.findViewById(R.id.tv_profile_gender)
        tvPhone = view.findViewById(R.id.tv_profile_phone)
        tvBhyt = view.findViewById(R.id.tv_profile_bhyt)
        tvAddress = view.findViewById(R.id.tv_profile_address)

        val btnEdit = view.findViewById<Button>(R.id.btnEditProfile)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        loadUserDataFromFirestore()

        btnEdit.setOnClickListener {
            showEditProfileDialog()
        }

        btnLogout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(requireContext(), "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserDataFromFirestore() {
        val userId = mAuth.currentUser?.uid
        if (userId == null) {
            // NẾU CHƯA ĐĂNG NHẬP: Hiển thị cảnh báo ngay trên màn hình để bạn biết
            tvName.text = "CHƯA ĐĂNG NHẬP"
            tvDob.text = "Vui lòng đăng nhập"
            tvGender.text = "Vui lòng đăng nhập"
            tvPhone.text = "Vui lòng đăng nhập"
            tvBhyt.text = "Vui lòng đăng nhập"
            tvAddress.text = "Vui lòng đăng nhập"
            return
        }

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (context == null || view == null) return@addOnSuccessListener

                if (document != null && document.exists()) {
                    tvName.text = document.getString("fullName")?.uppercase()
                    tvDob.text = document.getString("dob")
                    tvGender.text = document.getString("gender")
                    tvPhone.text = document.getString("phone")
                    tvBhyt.text = document.getString("bhyt")
                    tvAddress.text = document.getString("address")
                } else {
                    tvName.text = mAuth.currentUser?.displayName?.uppercase() ?: "CHƯA CẬP NHẬT"
                    tvDob.text = "Chưa cập nhật"
                    tvGender.text = "Chưa cập nhật"
                    tvPhone.text = "Chưa cập nhật"
                    tvBhyt.text = "Chưa cập nhật"
                    tvAddress.text = "Chưa cập nhật"
                }
            }
            .addOnFailureListener {
                if (context != null) Toast.makeText(requireContext(), "Lỗi tải hồ sơ!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val edtName = dialogView.findViewById<TextInputEditText>(R.id.edt_edit_name)
        val edtDob = dialogView.findViewById<TextInputEditText>(R.id.edt_edit_dob)
        val edtGender = dialogView.findViewById<TextInputEditText>(R.id.edt_edit_gender)
        val edtPhone = dialogView.findViewById<TextInputEditText>(R.id.edt_edit_phone)
        val edtBhyt = dialogView.findViewById<TextInputEditText>(R.id.edt_edit_bhyt)
        val edtAddress = dialogView.findViewById<TextInputEditText>(R.id.edt_edit_address)

        val btnSave = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_save_edit)
        val btnCancel = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_cancel_edit)

        edtName.setText(if (tvName.text == "CHƯA ĐĂNG NHẬP" || tvName.text == "ĐANG TẢI..." || tvName.text == "CHƯA CẬP NHẬT") "" else tvName.text)
        edtDob.setText(if (tvDob.text == "Vui lòng đăng nhập" || tvDob.text == "Đang tải..." || tvDob.text == "Chưa cập nhật") "" else tvDob.text)
        edtGender.setText(if (tvGender.text == "Vui lòng đăng nhập" || tvGender.text == "Đang tải..." || tvGender.text == "Chưa cập nhật") "" else tvGender.text)
        edtPhone.setText(if (tvPhone.text == "Vui lòng đăng nhập" || tvPhone.text == "Đang tải..." || tvPhone.text == "Chưa cập nhật") "" else tvPhone.text)
        edtBhyt.setText(if (tvBhyt.text == "Vui lòng đăng nhập" || tvBhyt.text == "Đang tải..." || tvBhyt.text == "Chưa cập nhật") "" else tvBhyt.text)
        edtAddress.setText(if (tvAddress.text == "Vui lòng đăng nhập" || tvAddress.text == "Đang tải..." || tvAddress.text == "Chưa cập nhật") "" else tvAddress.text)

        btnSave.setOnClickListener {
            val nameStr = edtName.text.toString().trim()
            val dobStr = edtDob.text.toString().trim()
            val genderStr = edtGender.text.toString().trim()
            val phoneStr = edtPhone.text.toString().trim()
            val bhytStr = edtBhyt.text.toString().trim()
            val addressStr = edtAddress.text.toString().trim()

            if (nameStr.isEmpty() || dobStr.isEmpty() || genderStr.isEmpty() || phoneStr.isEmpty() || bhytStr.isEmpty() || addressStr.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ tất cả thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🛡️ LƯỚI AN TOÀN MỚI: Báo lỗi to và rõ nếu phát hiện người dùng chưa đăng nhập
            val userId = mAuth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(requireContext(), "LỖI: Bạn chưa đăng nhập! Vui lòng qua tab Tài khoản để đăng nhập.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Báo hiệu hệ thống đang chạy
            Toast.makeText(requireContext(), "Đang đẩy dữ liệu lên hệ thống...", Toast.LENGTH_SHORT).show()

            val userMap = hashMapOf(
                "fullName" to nameStr,
                "dob" to dobStr,
                "gender" to genderStr,
                "phone" to phoneStr,
                "bhyt" to bhytStr,
                "address" to addressStr
            )

            db.collection("users").document(userId).set(userMap)
                .addOnSuccessListener {
                    if (context == null) return@addOnSuccessListener
                    Toast.makeText(requireContext(), "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show()

                    tvName.text = nameStr.uppercase()
                    tvDob.text = dobStr
                    tvGender.text = genderStr
                    tvPhone.text = phoneStr
                    tvBhyt.text = bhytStr
                    tvAddress.text = addressStr

                    alertDialog.dismiss()
                }
                .addOnFailureListener { e ->
                    // Nếu lỗi do Firebase Database, nó sẽ réo lên ở đây
                    Toast.makeText(requireContext(), "Lỗi Firebase: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        btnCancel.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }
}