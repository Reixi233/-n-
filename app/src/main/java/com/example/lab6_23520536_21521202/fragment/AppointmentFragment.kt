package com.example.lab6_23520536_21521202

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab6_23520536_21521202.databinding.FragmentAppointmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())

        loadAppointments()
    }

    private fun loadAppointments() {
        val userId = mAuth.currentUser?.uid ?: return

        db.collection("appointments")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                // 🛡️ LƯỚI AN TOÀN 1: Nếu người dùng đã vuốt sang tab khác (binding bị hủy) thì ngắt lệnh luôn, không vẽ giao diện nữa.
                if (_binding == null) return@addOnSuccessListener

                if (documents.isEmpty) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.rvAppointments.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.rvAppointments.visibility = View.VISIBLE
                    // TODO: Sau này sẽ set Adapter để hiển thị danh sách
                    Toast.makeText(requireContext(), "Có ${documents.size()} lịch khám", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // 🛡️ LƯỚI AN TOÀN 2: Dùng biến context thay vì requireContext() để tránh văng app nếu màn hình đã đóng khi bị lỗi mạng.
                if (context != null) {
                    Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}