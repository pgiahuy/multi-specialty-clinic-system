# Hệ thống Quản lý Phòng khám Đa Khoa Trực tuyến

Một hệ thống quản lý phòng khám toàn diện được thiết kế nhằm tối ưu hóa quy trình vận hành y tế, chăm sóc bệnh nhân và các tác vụ hành chính cho các cơ sở y tế đa chuyên khoa.

---

## Tổng quan Dự án

Dự án này là một ứng dụng web full-stack hỗ trợ các tính năng:
- **Quản lý Bệnh nhân**: Đăng ký, quản lý hồ sơ và theo dõi lịch sử y tế.
- **Đặt lịch Hẹn**: Đặt lịch khám trực tuyến với các bác sĩ chuyên khoa.
- **Hồ sơ Bệnh án Điện tử (EMR)**: Số hóa hồ sơ sức khỏe và kết quả xét nghiệm của bệnh nhân.
- **Quản lý Dược & Kho dược**: Danh mục thuốc, theo dõi tồn kho và cảnh báo hạn sử dụng.
- **Xử lý Thanh toán**: Thanh toán trực tuyến cho các dịch vụ y tế.
- **Thông báo**: Nhắc lịch hẹn và cập nhật đơn thuốc theo thời gian thực.
- **Phân tích & Báo cáo**: Số liệu thống kê toàn diện và thông tin chuyên sâu về sức khỏe cộng đồng.

---

## Tính năng Cốt lõi

### Dành cho Bệnh nhân
- Đăng ký tài khoản và quản lý thông tin cá nhân.
- Đặt lịch hẹn khám trực tuyến theo chuyên khoa.
- Theo dõi lịch sử khám bệnh và kết quả xét nghiệm.
- Thanh toán trực tuyến chi phí khám bệnh.
- Nhận nhắc lịch hẹn và thông tin đơn thuốc theo thời gian thực.
- Tư vấn y tế từ xa (Video call/Chat).

### Dành cho Bác sĩ & Nhân viên Y tế
- Quản lý lịch làm việc và danh sách lịch hẹn.
- Quản lý hồ sơ bệnh án điện tử của bệnh nhân.
- Quản lý và kê đơn thuốc trực tuyến.
- Hỗ trợ tư vấn, khám bệnh từ xa.

### Quản lý Dược & Kho dược
- Quản lý danh mục thuốc và theo dõi lượng tồn kho.
- Tự động cảnh báo thuốc sắp hết hạn sử dụng.
- Thông báo khi lượng hàng trong kho xuống mức thấp.
- Tự động trừ kho dựa trên đơn thuốc được kê.

### Thống kê & Báo cáo
- Báo cáo biểu đồ nhân khẩu học của bệnh nhân (độ tuổi, giới tính, chuyên khoa).
- Thống kê tần suất sử dụng các dịch vụ y tế.
- Theo dõi các mô hình bệnh tật phổ biến trong cộng đồng.
- Báo cáo doanh thu (tổng quan và chi tiết).

---

## Kiến trúc & Công nghệ Sử dụng

### Backend
- **Framework**: Spring MVC 6.2.16 + Spring Security 6.3.4
- **ORM**: Hibernate 6.6.1 với Jakarta Persistence
- **Cơ sở dữ liệu**: MySQL 8.4.0
- **API**: RESTful API với Jackson databind
- **Công cụ đóng gói (Build Tool)**: Maven
- **Phiên bản Java**: 17
- **Các thư viện bổ sung**:
  - Firebase Admin SDK
  - Cloudinary
  - JWT
  - Thymeleaf
  - Google API Client

### Frontend
- **Framework**: React 19.2.4
- **Điều hướng (Routing)**: React Router DOM 7.14.1
- **Thư viện UI**: Bootstrap 5.3.8 + PrimeReact 10.9.8
- **Quản lý trạng thái & Gọi API**: Axios
- **Biểu đồ (Charting)**: Chart.js 4.5.1
- **Thời gian thực (Real-time)**: Firebase 12.13.0
- **Xử lý Ngày/Tháng**: Moment.js 2.30.1
- **Xác thực mã hóa**: JWT Decode 4.0.0
- **Trình soạn thảo văn bản**: Quill 2.0.3
- **Công cụ đóng gói**: Create React App

---

## Cấu trúc Thư mục

```text
multi-specialty-clinic-system/
├── clinic/                      # Backend (Ứng dụng Spring Boot/MVC)
│   ├── src/main/java/com/hb//
│   │   ├── controllers/         # Các endpoint REST API
│   │   ├── service/             # Tầng xử lý logic nghiệp vụ (Business logic)
│   │   ├── repository/          # Tầng truy cập dữ liệu (Data access)
│   │   ├── pojo/                # Các thực thể dữ liệu (Entity models)
│   │   ├── dto/                 # Đối tượng chuyển đổi dữ liệu (Data transfer objects)
│   │   ├── mapper/              # Bộ ánh xạ DTO (MapStruct)
│   │   ├── enums/               # Các kiểu dữ liệu Enum
│   │   └── exception/           # Xử lý ngoại lệ tùy chỉnh (Custom exceptions)
│   ├── src/main/resources/
│   │   ├── application.properties # Cấu hình hệ thống
│   │   └── templates/           # Giao diện Thymeleaf
│   └── pom.xml                  # Quản lý dependency của Maven
│
├── client/                      # Frontend (Ứng dụng React)
│   ├── src/
│   │   ├── components/          # Các component React dùng chung
│   │   ├── pages/               # Các trang giao diện chính
│   │   ├── services/            # Các hàm gọi API service
│   │   ├── utils/               # Các hàm tiện ích bổ trợ
│   │   ├── App.js               # Component chính của ứng dụng
│   │   └── index.js             # Điểm khởi chạy hệ thống (Entry point)
│   ├── public/                  # Các tài nguyên tĩnh (Static assets)
│   └── package.json             # Quản lý dependency của NPM
│
└── README.md                    # File hướng dẫn này
```

## Các Phân hệ Chính

### Quản lý Người dùng
- Đăng ký, đăng nhập và xác thực người dùng (JWT + Đăng nhập mạng xã hội).
- Phân quyền truy cập dựa trên vai trò (Bệnh nhân, Bác sĩ, Quản trị viên).
- Quản lý FCM token để phục vụ gửi thông báo đẩy.

### Hệ thống Đặt lịch khám
- Tạo và quản lý lịch làm việc của bác sĩ.
- Đặt lịch hẹn và xác nhận lịch khám.
- Theo dõi trạng thái lịch hẹn (đang chờ, đã xác nhận, đã hoàn thành, đã hủy).
- Xếp hạng bác sĩ dựa trên tỷ lệ chuyển đổi lịch hẹn thành công.

### Hồ sơ Bệnh án
- Quản lý toàn diện lịch sử y tế của bệnh nhân.
- Quản lý kết quả xét nghiệm lâm sàng.
- Thống kê nhân khẩu học bệnh nhân và các mối quan hệ gia đình liên quan.

### Quản lý Dược phẩm & Kho hàng
- Quản lý các lô thuốc nhập kho kèm theo theo dõi ngày hết hạn.
- Nhật ký lưu trữ mọi biến động, thay đổi của kho hàng.
- Tự động gửi cảnh báo khi hàng sắp hết hoặc thuốc sắp quá hạn.

### Hệ thống Thanh toán
- Hỗ trợ nhiều phương thức thanh toán linh hoạt.
- Theo dõi và cập nhật trạng thái giao dịch theo thời gian thực.
- Xuất báo cáo doanh thu tổng hợp.

### Hệ thống Thông báo
- Tích hợp dịch vụ Firebase Cloud Messaging (FCM).
- Gửi thông báo nhắc lịch khám định kỳ.
- Thông báo đơn thuốc mới cho bệnh nhân.
- Lưu nhật ký lịch sử thông báo vào cơ sở dữ liệu.


## Tính năng Bảo mật
- Sử dụng Spring Security để phân quyền dựa trên vai trò.
- Xác thực người dùng thông qua mã token mã hóa JWT.
- Tích hợp cơ chế đăng nhập bằng tài khoản MXH bên thứ ba (Google, Facebook).

---

## Sơ đồ Cơ sở Dữ liệu

<img width="1962" height="1277" alt="a" src="https://github.com/user-attachments/assets/59de2c27-47ec-46e7-9f9c-b85a11d3b180" />


Các thực thể cốt lõi bao gồm:
- **User**: Tài khoản người dùng phục vụ đăng nhập và phân quyền.
- **Patient**: Thông tin nhân khẩu học và hồ sơ y tế bệnh nhân.
- **Appointment**: Dữ liệu điều phối và quản lý lịch hẹn khám.
- **Medicine**: Danh mục thuốc trong kho dược.
- **MedicineBatch**: Theo dõi chi tiết từng lô thuốc nhập và hạn sử dụng.
- **Payment**: Lưu trữ nhật ký các giao dịch thanh toán hóa đơn.
- **Notification**: Quản lý thông báo gửi tới người dùng.
- **LabTest**: Hồ sơ lưu trữ kết quả xét nghiệm và cận lâm sàng.
- **InventoryLog**: Nhật ký ghi nhận mọi biến động xuất-nhập kho.

---

## Các Endpoint API mẫu

### Xác thực & Tài khoản
- `POST /auth/register` - Đăng ký tài khoản mới
- `POST /auth/login` - Đăng nhập vào hệ thống
- `POST /auth/logout` - Đăng xuất khỏi hệ thống

### Lịch hẹn khám
- `GET /api/appointments` - Lấy danh sách các lịch hẹn
- `POST /api/appointments` - Tạo lịch hẹn mới
- `GET /api/appointments/{id}` - Xem chi tiết một lịch hẹn cụ thể
- `PUT /api/appointments/{id}` - Cập nhật thông tin lịch hẹn

### Quản lý Thuốc (Admin)
- `GET /admin/medicines` - Lấy danh sách các loại thuốc
- `POST /admin/medicines` - Thêm mới một loại thuốc vào danh mục
- `DELETE /admin/medicines/{id}` - Xóa thuốc khỏi hệ thống

### Thanh toán
- `GET /api/payments` - Xem danh sách hóa đơn giao dịch
- `POST /api/payments` - Tạo hóa đơn thanh toán mới
- `GET /api/payments/{id}` - Xem chi tiết giao dịch cụ thể

### Báo cáo Thống kê
- `GET /api/stats/patients` - Thống kê nhân khẩu học bệnh nhân
- `GET /api/stats/revenue` - Báo cáo doanh thu tài chính
- `GET /api/stats/services` - Thống kê tần suất dùng các dịch vụ y tế

---

Trạng thái: Đang trong quá trình phát triển.

Cập nhật lần cuối: Tháng 6 năm 2026
