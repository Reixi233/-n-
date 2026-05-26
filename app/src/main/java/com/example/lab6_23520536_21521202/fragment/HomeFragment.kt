package com.example.lab6_23520536_21521202.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lab6_23520536_21521202.ChatFragment
import com.example.lab6_23520536_21521202.R
import com.example.lab6_23520536_21521202.auth.LoginActivity
import com.example.lab6_23520536_21521202.avtivity.QrScannerActivity
import com.example.lab6_23520536_21521202.avtivity.SelectDepartmentActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ==========================================
        // 1. CÁ NHÂN HÓA LỜI CHÀO TRÊN TIÊU ĐỀ
        // ==========================================
        val currentUser = FirebaseAuth.getInstance().currentUser
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar

        if (currentUser != null) {
            val fullName = currentUser.displayName ?: "bạn"
            actionBar?.title = "Hi, $fullName 👋"
        } else {
            actionBar?.title = "Medical App"
        }

        // ==========================================
        // 2. ÁNH XẠ TOÀN BỘ CÁC NÚT CHỨC NĂNG
        // ==========================================
        val btnHoTroKham = view.findViewById<LinearLayout>(R.id.btn_ho_tro_kham)
        val btnDatKhamMain = view.findViewById<MaterialButton>(R.id.btn_dat_kham_main)
        val btnQuetQr = view.findViewById<LinearLayout>(R.id.btn_quet_qr)
        val btnHoSo = view.findViewById<LinearLayout>(R.id.btn_ho_so)
        val btnLichSu = view.findViewById<LinearLayout>(R.id.btn_lich_su)
        val btnCaiDat = view.findViewById<LinearLayout>(R.id.btn_cai_dat)

        // ==========================================
        // 3. XỬ LÝ SỰ KIỆN CLICK (ĐÃ BỌC BẢO VỆ ĐĂNG NHẬP)
        // ==========================================

        btnHoTroKham?.setOnClickListener {
            requireLogin {
                val chatFragment = ChatFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, chatFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        btnDatKhamMain?.setOnClickListener {
            requireLogin {
                val intent = Intent(requireContext(), SelectDepartmentActivity::class.java)
                startActivity(intent)
            }
        }

        btnQuetQr?.setOnClickListener {
            requireLogin {
                val intent = Intent(requireContext(), QrScannerActivity::class.java)
                startActivity(intent)
            }
        }

        btnHoSo?.setOnClickListener {
            requireLogin {
                val profileFragment = ProfileFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, profileFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        btnCaiDat?.setOnClickListener {
            requireLogin {
                val settingsFragment = SettingsFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, settingsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        btnLichSu?.setOnClickListener {
            requireLogin {
                Toast.makeText(requireContext(), "Lịch sử khám đang trống!", Toast.LENGTH_SHORT).show()
            }
        }

        // ==========================================
        // 4. CÁC NÚT THỰC SỰ CHƯA PHÁT TRIỂN (MOCK DATA)
        // ==========================================
        val unimplementedButtons = listOf(
            view.findViewById<LinearLayout>(R.id.btn_tra_cuu_hoa_don),
            view.findViewById<LinearLayout>(R.id.btn_tra_cuu_kq),
            view.findViewById<LinearLayout>(R.id.btn_lich_su_hien_mau),
            view.findViewById<LinearLayout>(R.id.btn_tra_cuu_ban_do)
        )

        for (btn in unimplementedButtons) {
            btn?.setOnClickListener {
                requireLogin {
                    Toast.makeText(requireContext(), "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ==========================================
        // 5. HIỂN THỊ POPUP THÔNG TIN KHI VÀO TRANG CHỦ
        // ==========================================
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("THÔNG TIN")

        // ĐÃ SỬA: Dùng 3 dấu ngoặc kép để bọc văn bản dài siêu an toàn
        builder.setMessage("""
            + Địa điểm khám bệnh ngoài giờ hành chính: Lầu 2, Khu khám bệnh theo yêu cầu, tòa nhà Viện Chấn thương Chỉnh hình, Bệnh viện Quận y 175.
            
            + Hiện tại, Bệnh viện Quận y 175 chỉ hỗ trợ đặt lịch khám online đối với các chuyên khoa. Dịch vụ khám sức khỏe để làm giấy tờ chưa áp dụng hình thức đặt lịch trực tuyến, quý khách vui lòng đến trực tiếp bệnh viện để được hỗ trợ.
            
            + Để tầm soát lao phục vụ nhập cảnh Nhật Bản, khách hàng vui lòng đăng ký trực tiếp tại Phòng khám Sàng lọc Lao - Bệnh viện Quận y 175.
            
            📍 Địa điểm: Khu vực Xét nghiệm theo yêu cầu - tầng trệt nhà gửi xe máy, cổng Nguyễn Thái Sơn.
            
            📌 Lưu ý: Chưa hỗ trợ đặt lịch dịch vụ này qua ứng dụng.
        """.trimIndent())

        builder.setPositiveButton("Tôi đã hiểu!") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setTextColor(android.graphics.Color.parseColor("#388E3C"))
    }

    // ==========================================
    // HÀM KIỂM TRA ĐĂNG NHẬP TRƯỚC KHI ĐI TIẾP
    // ==========================================
    private fun requireLogin(action: () -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Đã đăng nhập -> cho phép thực hiện hành động
            action()
        } else {
            // Chưa đăng nhập -> đá sang trang Đăng nhập
            Toast.makeText(requireContext(), "Vui lòng đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}