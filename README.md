# Tổng hợp Lệnh Redis và Giải thích (Tiếng Việt)

Tài liệu này cung cấp danh sách các lệnh Redis phổ biến, bao gồm cách sử dụng và giải thích chi tiết bằng tiếng Việt. Các lệnh được thực thi thông qua `redis-cli` hoặc trong container Docker chạy Redis server.

## 1. Các Lệnh Cơ Bản để Tương Tác với Redis trong Docker

### 1.1. Mở Shell Bash trong Container Redis
```bash
docker exec -it redis bash
```
- **Mục đích**: Mở một phiên giao tiếp Bash tương tác bên trong container Docker có tên `redis`.
- **Giải thích**:
  - `docker exec`: Chạy lệnh trong container đang hoạt động.
  - `-it`: Kết hợp `-i` (giữ STDIN mở) và `-t` (cấp phát pseudo-TTY) để tương tác qua shell.
  - `redis`: Tên container chạy Redis server.
  - `bash`: Khởi động shell Bash để thực thi các lệnh tiếp theo.

### 1.2. Chạy Redis CLI
```bash
redis-cli
```
- **Mục đích**: Khởi động giao diện dòng lệnh Redis (Redis CLI) để tương tác với Redis server.
- **Giải thích**:
  - Kết nối với Redis server (mặc định tại `localhost:6379`).
  - Cung cấp giao diện để nhập các lệnh Redis.
  - Nếu server yêu cầu mật khẩu hoặc dùng host/port khác, sử dụng: `redis-cli -h <host> -p <port> -a <password>`.

### 1.3. Kiểm tra Kết nối với Redis Server
```bash
ping
```
- **Mục đích**: Kiểm tra xem Redis server có phản hồi không.
- **Giải thích**:
  - Gửi yêu cầu đơn giản đến server.
  - Trả về `PONG` nếu kết nối thành công, giúp xác minh server đang hoạt động.

### 1.4. Xóa Toàn bộ Dữ liệu trong Database Hiện tại
```bash
flushdb
```
- **Mục đích**: Xóa tất cả khóa (keys) trong database Redis hiện tại.
- **Giải thích**:
  - Xóa dữ liệu trong database đang chọn (mặc định là database 0).
  - Không ảnh hưởng đến các database khác.
  - Cảnh báo: Lệnh này xóa dữ liệu vĩnh viễn, cần sao lưu trước nếu cần.

## 2. Các Lệnh Quản lý Dữ liệu trong Redis

### 2.1. Thêm hoặc Cập nhật Khóa-Giá trị
```bash
set <key> <value>
```
- **Mục đích**: Lưu một giá trị vào một khóa trong Redis.
- **Giải thích**:
  - Tạo mới hoặc cập nhật giá trị cho khóa `<key>` với giá trị `<value>` (chuỗi, số, v.v.).
  - Ví dụ: `set username "Grok"` lưu giá trị `"Grok"` vào khóa `username`.

### 2.2. Lấy Giá trị của Khóa
```bash
get <key>
```
- **Mục đích**: Lấy giá trị được lưu trong một khóa.
- **Giải thích**:
  - Trả về giá trị của khóa `<key>` hoặc `nil` nếu khóa không tồn tại.
  - Ví dụ: `get username` trả về `"Grok"`.

### 2.3. Xóa Khóa
```bash
del <key1> [<key2> ...]
```
- **Mục đích**: Xóa một hoặc nhiều khóa và giá trị liên quan.
- **Giải thích**:
  - Xóa các khóa được chỉ định và trả về số lượng khóa đã xóa.
  - Ví dụ: `del username` xóa khóa `username`.

### 2.4. Kiểm tra Tồn tại của Khóa
```bash
exists <key>
```
- **Mục đích**: Kiểm tra xem một khóa có tồn tại trong database không.
- **Giải thích**:
  - Trả về `1` nếu khóa tồn tại, `0` nếu không.
  - Ví dụ: `exists username` trả về `1` nếu khóa `username` tồn tại.

### 2.5. Đặt Thời gian Sống cho Khóa
```bash
expire <key> <seconds>
```
- **Mục đích**: Đặt thời gian sống (TTL - Time To Live) cho một khóa.
- **Giải thích**:
  - Khóa sẽ tự động bị xóa sau `<seconds>` giây.
  - Ví dụ: `expire username 60` đặt khóa `username` hết hạn sau 60 giây.

### 2.6. Kiểm tra Thời gian Sống của Khóa
```bash
ttl <key>
```
- **Mục đích**: Xem thời gian sống còn lại của một khóa.
- **Giải thích**:
  - Trả về số giây còn lại trước khi khóa hết hạn, hoặc `-1` nếu không có thời hạn, `-2` nếu khóa không tồn tại.
  - Ví dụ: `ttl username` trả về số giây còn lại.

## 3. Các Lệnh Quản lý Database

### 3.1. Chọn Database
```bash
select <index>
```
- **Mục đích**: Chuyển sang một database khác trong Redis.
- **Giải thích**:
  - Redis hỗ trợ nhiều database (mặc định từ 0 đến 15, tùy cấu hình).
  - Ví dụ: `select 1` chuyển sang database số 1.

### 3.2. Xóa Toàn bộ Dữ liệu trong Tất cả Database
```bash
flushall
```
- **Mục đích**: Xóa toàn bộ dữ liệu trong tất cả database của Redis instance.
- **Giải thích**:
  - Xóa tất cả khóa trong mọi database, không thể hoàn tác.
  - Cảnh báo: Cần thận trọng khi sử dụng trên môi trường sản xuất.

## 4. Các Lệnh Làm việc với Danh sách (Lists)

### 4.1. Thêm Phần tử vào Danh sách
```bash
lpush <key> <value1> [<value2> ...]
```
- **Mục đích**: Thêm một hoặc nhiều phần tử vào đầu danh sách được lưu tại khóa `<key>`.
- **Giải thích**:
  - Tạo danh sách nếu khóa chưa tồn tại.
  - Ví dụ: `lpush mylist "item1"` thêm `"item1"` vào đầu danh sách `mylist`.

### 4.2. Lấy Phần tử từ Danh sách
```bash
lpop <key>
```
- **Mục đích**: Lấy và xóa phần tử đầu tiên trong danh sách.
- **Giải thích**:
  - Trả về phần tử đầu tiên và xóa nó khỏi danh sách.
  - Ví dụ: `lpop mylist` trả về `"item1"` và xóa nó.

### 4.3. Lấy Độ dài Danh sách
```bash
llen <key>
```
- **Mục đích**: Đếm số phần tử trong danh sách.
- **Giải thích**:
  - Trả về số lượng phần tử trong danh sách tại khóa `<key>`.
  - Ví dụ: `llen mylist` trả về số phần tử trong `mylist`.

## 5. Các Lệnh Làm việc với Tập hợp (Sets)

### 5.1. Thêm Phần tử vào Tập hợp
```bash
sadd <key> <member1> [<member2> ...]
```
- **Mục đích**: Thêm một hoặc nhiều phần tử vào tập hợp tại khóa `<key>`.
- **Giải thích**:
  - Tập hợp không chứa phần tử trùng lặp.
  - Ví dụ: `sadd myset "member1"` thêm `"member1"` vào tập hợp `myset`.

### 5.2. Lấy Tất cả Phần tử trong Tập hợp
```bash
smembers <key>
```
- **Mục đích**: Lấy tất cả phần tử trong tập hợp.
- **Giải thích**:
  - Trả về danh sách các phần tử trong tập hợp tại khóa `<key>`.
  - Ví dụ: `smembers myset` trả về tất cả thành viên của `myset`.

## 6. Các Lệnh Quản lý Server

### 6.1. Lấy Thông tin Server
```bash
info
```
- **Mục đích**: Lấy thông tin chi tiết về trạng thái Redis server.
- **Giải thích**:
  - Trả về thông tin như phiên bản, bộ nhớ, số lượng client, v.v.
  - Ví dụ: `info memory` trả về thông tin về bộ nhớ.

### 6.2. Lưu Dữ liệu ra Đĩa
```bash
save
```
- **Mục đích**: Lưu dữ liệu Redis vào đĩa (tạo snapshot).
- **Giải thích**:
  - Lưu trạng thái hiện tại của database vào file dump (thường là `dump.rdb`).
  - Dùng để backup dữ liệu.

### 6.3. Thoát Redis CLI
```bash
exit
```
- **Mục đích**: Thoát khỏi giao diện Redis CLI.
- **Giải thích**:
  - Đóng kết nối với Redis server và quay lại shell.

## Ví dụ Quy trình Sử dụng

1. Mở terminal và truy cập container Redis:
   ```bash
   docker exec -it redis bash
   ```

2. Khởi động Redis CLI:
   ```bash
   redis-cli
   ```

3. Kiểm tra kết nối:
   ```bash
   ping
   ```
   Kết quả: `PONG`

4. Lưu và lấy dữ liệu:
   ```bash
   set mykey "Hello Redis"
   get mykey
   ```
   Kết quả: `"Hello Redis"`

5. Xóa database hiện tại:
   ```bash
   flushdb
   ```
   Kết quả: `OK`

6. Thoát Redis CLI và container:
   ```bash
   exit
   exit
   ```

## Lưu ý
- Đảm bảo container `redis` đang chạy (`docker ps` để kiểm tra).
- Nếu Redis yêu cầu xác thực, thêm `-a <password>` khi chạy `redis-cli`.
- Các lệnh như `flushdb` và `flushall` xóa dữ liệu vĩnh viễn, cần sao lưu trước.
- Redis CLI hỗ trợ nhiều lệnh khác cho các kiểu dữ liệu phức tạp như Hashes, Sorted Sets, v.v. Xem thêm tại [Redis Documentation](https://redis.io/commands).
