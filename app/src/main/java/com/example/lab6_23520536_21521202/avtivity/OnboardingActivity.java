package com.example.lab6_23520536_21521202.avtivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.lab6_23520536_21521202.R;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout layoutIndicators;
    private MaterialButton btnNext;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LUÔN LUÔN HIỂN THỊ MÀN HÌNH NÀY KHI MỞ APP (Đã xóa SharedPreferences)
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        layoutIndicators = findViewById(R.id.layoutIndicators);
        btnNext = findViewById(R.id.btnNext);
        tvSkip = findViewById(R.id.tvSkip);

        List<OnboardingItemEmoji> items = new ArrayList<>();
        items.add(new OnboardingItemEmoji("⏰", "Đặt lịch dễ dàng", "Chủ động chọn ngày, giờ và bác sĩ khám bệnh mà không cần chờ đợi."));
        items.add(new OnboardingItemEmoji("💳", "Thanh toán tiện lợi", "Hỗ trợ nhiều phương thức thanh toán trực tuyến an toàn và nhanh chóng."));
        items.add(new OnboardingItemEmoji("📁", "Quản lý hồ sơ", "Lưu trữ và theo dõi hồ sơ sức khỏe của bạn và gia đình mọi lúc mọi nơi."));
        items.add(new OnboardingItemEmoji("📰", "Tin tức y tế", "Cập nhật liên tục các kiến thức chăm sóc sức khỏe từ chuyên gia."));

        viewPager.setAdapter(new OnboardingAdapterEmoji(items));

        setupIndicators(items.size());
        setCurrentIndicator(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
                if (position == items.size() - 1) {
                    btnNext.setText("BẮT ĐẦU");
                } else {
                    btnNext.setText("TIẾP THEO");
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() + 1 < items.size()) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                navigateToMain();
            }
        });

        tvSkip.setOnClickListener(v -> navigateToMain());
    }

    private void setupIndicators(int count) {
        for (int i = 0; i < count; i++) {
            TextView textView = new TextView(this);
            textView.setText("○");
            textView.setTextSize(24f);
            textView.setTextColor(Color.GRAY);
            textView.setPadding(8, 0, 8, 0);
            layoutIndicators.addView(textView);
        }
    }

    private void setCurrentIndicator(int index) {
        for (int i = 0; i < layoutIndicators.getChildCount(); i++) {
            TextView textView = (TextView) layoutIndicators.getChildAt(i);
            if (i == index) {
                textView.setText("●");
                textView.setTextColor(Color.parseColor("#388E3C"));
            } else {
                textView.setText("○");
                textView.setTextColor(Color.GRAY);
            }
        }
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

class OnboardingItemEmoji {
    String emoji, title, description;
    public OnboardingItemEmoji(String emoji, String title, String description) {
        this.emoji = emoji;
        this.title = title;
        this.description = description;
    }
}

class OnboardingAdapterEmoji extends RecyclerView.Adapter<OnboardingAdapterEmoji.ViewHolder> {
    List<OnboardingItemEmoji> items;
    public OnboardingAdapterEmoji(List<OnboardingItemEmoji> items) { this.items = items; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvEmoji.setText(items.get(position).emoji);
        holder.tvTitle.setText(items.get(position).title);
        holder.tvDescription.setText(items.get(position).description);
    }

    @Override
    public int getItemCount() { return items.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvTitle, tvDescription;
        public ViewHolder(View view) {
            super(view);
            tvEmoji = view.findViewById(R.id.tvEmoji);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
        }
    }
}