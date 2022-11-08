class App {

    static DOMAIN_API = "http://localhost:9091";
    static BASE_URL_HISTORY_TRANSFER = this.DOMAIN_API + "/transfers";
    static BASE_URL_API_HISTORY_TRANSFER = this.DOMAIN_API + "/api/transfers";
    static BASE_URL_CUSTOMER = this.DOMAIN_API + "/api/customers";
    static BASE_URL_AUTH = this.DOMAIN_API + "/api/auth";

    static AlertMessageEn = class {
        static SUCCESS_CREATED = "Successful data generation !";
        static SUCCESS_UPDATED = "Data update successful !";
        static SUCCESS_DEPOSIT = "Deposit transaction successful !";
        static SUCCESS_WITHDRAW = "Withdrawal transaction successful !";
        static SUCCESS_TRANSFER = "Transfer transaction successful !";
        static SUCCESS_DEACTIVATE = "Deactivate the customer successfully !";

        static ERROR_400 = "The operation failed, please check the data again.";
        static ERROR_401 = "Unauthorized - Your access token has expired or is not valid.";
        static ERROR_403 = "Forbidden - You are not authorized to access this resource.";
        static ERROR_404 = "Not Found - The resource has been removed or does not exist";
        static ERROR_500 = "Internal Server Error - The server system is having problems or cannot be accessed.";

        static ERROR_LOADING_PROVINCE = "Loading list of provinces - cities failed !";
        static ERROR_LOADING_DISTRICT = "Loading list of district - ward failed !"
        static ERROR_LOADING_WARD = "Loading list of wards - communes - towns failed !";
    }

    static AlertMessageVi = class {
        static SUCCESS_CREATED = "Tạo dữ liệu thành công !";
        static SUCCESS_UPDATED = "Cập nhật dữ liệu thành công !";
        static SUCCESS_DEPOSIT = "Giao dịch gửi tiền thành công !";
        static SUCCESS_WITHDRAW = "Giao dịch rút tiền thành công !";
        static SUCCESS_TRANSFER = "Giao dịch chuyển khoản thành công !";
        static SUCCESS_DEACTIVATE = "Hủy kích hoạt khách hàng thành công !";
        static SUCCESS_SUSPENDED = "Tài khoản đã bị chặn khởi hệ thống thành công !";

        static ERROR_WITHDRAW = "Số tiền trong tài khoản của quý khách không đủ, vui lòng kiểm tra lại.";
        static ERROR_400 = "Thao tác không thành công, vui lòng kiểm tra lại dữ liệu.";
        static ERROR_401 = "Unauthorized - Access Token của bạn hết hạn hoặc không hợp lệ.";
        static ERROR_403 = "Forbidden - Bạn không được quyền truy cập tài nguyên này.";
        static ERROR_404 = "Not Found - Tài nguyên này đã bị xóa hoặc không tồn tại";
        static ERROR_500 = "Internal Server Error - Hệ thống Server đang có vấn đề hoặc không truy cập được.";

        static ERROR_LOADING_PROVINCE = "Tải danh sách tỉnh - thành phố không thành công !";
        static ERROR_LOADING_DISTRICT = "Tải danh sách quận - phường - huyện không thành công !";
        static ERROR_LOADING_WARD = "Tải danh sách phường - xã - thị trấn không thành công !";
    }

    static SweetAlert = class {

        static showAlertSuccess(t) {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: t,
                showConfirmButton: false,
                timer: 1500
            })
        }

        static showAlertError(t) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: t,
                showConfirmButton: true
            })
        }

        static showSuspendedConfirmDialog() {
            return Swal.fire({
                icon: 'warning',
                text: 'Are you sure to suspend the selected customer ?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, please suspend this client !',
                cancelButtonText: 'Cancel',
            })
        }
    }

    static IziToast = class {
        static showSuccessAlert(m) {
            iziToast.success({
                title: 'OK',
                position: 'topRight',
                timeout: 2500,
                message: m
            });
        }

        static showErrorAlert(m) {
            iziToast.error({
                title: 'Error',
                position: 'topRight',
                timeout: 2500,
                message: m
            });
        }
    }

    static renderRowCustomer(customer) {
        let str = `
            <tr id="tr_${customer.id}" data-id="${customer.id}">
                <td>
                    <span class="select-tab unselected"></span>
                </td>
                <td>${customer.id}</td>
                <td>${customer.fullName}</td>
                <td>${customer.email}</td>
                <td class="text-center">${customer.phone}</td>
                <td class="text-end num-space">${customer.balance}</td>
                <td>${customer.locationRegion.provinceName}</td>
                <td>${customer.locationRegion.districtName}</td>
                <td>${customer.locationRegion.wardName}</td>
                <td>${customer.locationRegion.address}</td>
            </tr>
        `;

        return str;
    }

    static renderFooterButton(customerId) {
        let str = `
            <button class="btn btn-secondary update" id="btnShowUpdateModal" data-id="${customerId}">
                <i class="fas fa-pencil-alt"></i>
                Update
            </button>
            <button class="btn btn-success deposit" id="btnShowDepositModal" data-id="${customerId}">
                <i class="fas fa-pencil-alt"></i>
                Deposit
            </button>
            <button class="btn btn-warning withdraw" id="btnShowWithdrawModal" data-id="${customerId}">
                <i class="fas fa-pencil-alt"></i>
                Withdraw
            </button>
            <button class="btn btn-primary transfer" id="btnShowTransferModal" data-id="${customerId}">
                <i class="fas fa-pencil-alt"></i>
                Transfer
            </button>
            <button class="btn btn-danger delete" id="btnConfirmDelete" data-id="${customerId}">
                <i class="fas fa-pencil-alt"></i>
                Delete
            </button>
        `;

        return str;
    }


    static renderRowTransferHistory(obj) {
        let str = `
            <tr id="tr_${obj.id}">
                <td>${obj.id}</td>
                <td>${obj.createdAt}</td>
                <td>${obj.senderId}</td>
                <td>${obj.senderName}</td>
                <td>${obj.recipientId}</td>
                <td>${obj.recipientName}</td>
                <td class="text-end num-space">${obj.transferAmount}</td>
                <td class="text-end num-space">${obj.fees}</td>
                <td class="text-end num-space">${obj.feesAmount}</td>
            </tr>
        `;

        return str;
    }
}



class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}


class Customer {
    constructor(id, fullName, email, phone, balance, locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.locationRegion = locationRegion;
    }
}

class Sender extends Customer {
    constructor() {
        super();
    }
}

class Recipient extends Customer {
    constructor() {
        super();
    }
}

class Withdraw {
    constructor(id, customerId, transactionAmount) {
        this.id = id;
        this.customerId =customerId;
        this.transactionAmount = transactionAmount;
    }
}

class Deposit {
    constructor(id, customerId, transactionAmount) {
        this.id = id;
        this.customerId =customerId;
        this.transactionAmount = transactionAmount;
    }
}

class Transfer {
    constructor(id, senderId, recipientId, transferAmount) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.transferAmount = transferAmount;
    }
}