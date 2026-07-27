# Hệ thống Quản lý Phòng khám Đa khoa

Một website quản lý phòng khám đa chuyên khoa. Phần backend viết bằng Spring MVC, frontend bằng React, dữ liệu lưu trên MySQL. Hệ thống phục vụ cả phía bệnh nhân (đặt lịch, khám, thanh toán online) lẫn phía phòng khám (quản lý bác sĩ, hồ sơ bệnh án, kho thuốc, thống kê).

## Tính năng

**Bệnh nhân** đăng ký tài khoản bằng email hoặc đăng nhập qua Google, đặt và huỷ lịch khám theo chuyên khoa, xem lại hồ sơ khám, đơn thuốc và kết quả xét nghiệm. Thanh toán hỗ trợ MoMo, VNPay hoặc tiền mặt tại quầy. Trong lúc chờ, bệnh nhân có thể chat trực tiếp với bác sĩ và nhận thông báo khi có lịch hẹn hoặc đơn thuốc/ thanh toán mới.

**Bác sĩ** đăng ký lịch làm việc theo ca, xem danh sách bệnh nhân trong ngày, lập hồ sơ bệnh án, chỉ định xét nghiệm và kê đơn thuốc.

**Nhân viên tiếp nhận và thủ kho** phụ trách phần quầy: tiếp nhận lịch hẹn, xuất hoá đơn, thu tiền mặt, quản lý kho thuốc - nhập lô, theo dõi hạn dùng, cảnh báo khi sắp hết, và tự trừ kho theo đơn đã kê.

**Quản trị viên** quản lý các danh mục (chuyên khoa, phòng, khu vực, ca làm việc, bác sĩ, bệnh nhân, thuốc) và xem báo cáo thống kê: doanh thu, cơ cấu bệnh nhân theo độ tuổi/giới tính/chuyên khoa, xếp hạng bác sĩ và tồn kho.

## Công nghệ

Backend đóng gói thành file WAR chạy trên Tomcat; frontend là ứng dụng React tạo bằng Create React App.

Backend

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java 17 |
| Framework | Spring MVC, Spring Security |
| ORM | Hibernate |
| CSDL | MySQL |
| Build | Maven |
| Khác | JWT (có refresh token), Firebase Admin, Cloudinary, Thymeleaf |

Frontend

| Thành phần | Công nghệ |
|---|---|
| Framework | React (Create React App) |
| Gọi API | Axios |
| Giao diện | Bootstrap, PrimeReact |
| Biểu đồ | Chart.js |
| Realtime | Firebase (FCM) |


## Cấu trúc thư mục

```text
multi-specialty-clinic-system/
├── clinic/                      # Backend Spring MVC (đóng gói WAR)
│   ├── src/main/java/com/hb/
│   │   ├── configs/             # Cấu hình Spring, Security, Firebase, MoMo, VNPay
│   │   ├── controllers/         # Controller MVC và REST API (thư mục api/)
│   │   ├── service/             # Logic nghiệp vụ
│   │   ├── repository/          # Truy cập dữ liệu qua Hibernate
│   │   ├── pojo/                # Entity ánh xạ CSDL
│   │   ├── dto/  mapper/        # DTO và bộ ánh xạ Entity-DTO
│   │   ├── enums/  filters/     # Enum dùng chung, JWT filter
│   │   └── exception/
│   ├── src/main/resources/      # databases.properties, configs.properties, templates
│   ├── db/init.sql              # Script khởi tạo cơ sở dữ liệu
│   └── pom.xml
│
└── client/                      # Frontend React
    ├── src/
    │   ├── components/          # Component dùng chung
    │   ├── configs/             # Apis.js, Contexts.js, firebaseConfig.js
    │   ├── reducers/            # Quản lý trạng thái người dùng
    │   └── screens/             # Màn hình theo vai trò: Doctor, Patient,
    │                            #   Reception, StoreKeeper, Home, User
    └── package.json
```

## Cài đặt & Chạy

Cần Java 17, Maven, Node.js 18+, npm và MySQL 8.

**Cơ sở dữ liệu** - tạo database và import script khởi tạo:

```bash
mysql -u root -p -e "CREATE DATABASE clinicdb CHARACTER SET utf8mb4;"
mysql -u root -p clinicdb < clinic/db/init.sql
```

Sửa thông tin kết nối trong `clinic/src/main/resources/databases.properties`, và điền các khoá dịch vụ (Firebase, Cloudinary, MoMo, VNPay, Google/Facebook) trong `configs.properties`.

**Backend** - build ra file WAR rồi triển khai lên Tomcat 10 trở lên:

```bash
cd clinic
mvn clean package
```

**Frontend**:

```bash
cd client
npm install
npm start
```

Frontend chạy ở `http://localhost:3000`. Nếu muốn dùng Docker, mỗi thư mục `clinic/` và `client/` đã có sẵn Dockerfile để build image.

Pipeline CI/CD trong `.github/workflows/cicd.yml` hiện đã tắt vì dự án đã hoàn thành.

## Cơ sở dữ liệu

<img width="1962" height="1277" alt="ERD" src="https://github.com/user-attachments/assets/59de2c27-47ec-46e7-9f9c-b85a11d3b180" />

| Bảng | Mô tả |
|---|---|
| User, Patient | Tài khoản và hồ sơ bệnh nhân |
| Doctor, Specialty | Bác sĩ và chuyên khoa (quan hệ nhiều-nhiều) |
| Schedule, Shift | Lịch làm việc và ca của bác sĩ |
| Appointment | Lịch hẹn khám |
| MedicalRecord | Hồ sơ bệnh án |
| Prescription, PrescriptionItem | Đơn thuốc và chi tiết |
| Medicine, MedicineBatch | Thuốc và lô nhập kho |
| InventoryLog | Nhật ký xuất-nhập kho |
| LabResult, LabResultDetail | Kết quả xét nghiệm |
| Payment, PaymentItem | Giao dịch thanh toán |
| Conversation, ChatMessage | Chat bác sĩ-bệnh nhân |
| Notification | Thông báo đẩy |

## Một số Endpoint API

```
POST   /api/auth/register        Đăng ký
POST   /api/auth/login           Đăng nhập
POST   /api/auth/refresh         Làm mới token

GET    /api/appointments         Danh sách lịch hẹn
POST   /api/appointments         Đặt lịch hẹn
GET    /api/appointments/{id}    Chi tiết lịch hẹn

POST   /api/payments             Tạo giao dịch (MoMo/VNPay/tiền mặt)
GET    /api/stats/revenue        Báo cáo doanh thu
```

Danh sách đầy đủ nằm trong `clinic/src/main/java/com/hb/controllers/`.

## Nhóm phát triển

- Gia Huy - [@pgiahuy](https://github.com/pgiahuy)
- Bao Nguyen - baondq205

---

Dự án đã hoàn thành. Cập nhật lần cuối tháng 7 năm 2026.
