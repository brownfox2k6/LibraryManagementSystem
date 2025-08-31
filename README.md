# Library Management Application [![starline](https://starlines.qoo.monster/assets/brownfox2k6/LibraryManagementApplication)](https://github.com/brownfox2k6/LibraryManagementApplication)

- **Course:** Object-oriented programming
- **Course Code:** 2425H_INT2204_60
- **Semester:** Summer 2024–2025

## Contributors
- **24021400 Nguyễn Trọng Đại** – [brownfox2k6](https://github.com/brownfox2k6)
- **24021408 Lục Thị Diệp** – [DiepLuc](https://github.com/DiepLuc)

## Brief Description
An application that helps users search for books, view detailed information, and manage borrowing activities. It integrates with the Google Books API for accurate book data, and supports QR code generation for quick access. Members can request to borrow books and track their borrowing history, while admins can manage stock and monitor all borrow records.

## Demo Video
[Demo ứng dụng Java quản lý thư viện (library management) - YouTube](https://youtu.be/AypL7hUO1Is)

## UML Class Diagram

<details><summary><strong>[Image] com.application.librarymanagement</strong></summary>

![](readme-images/uml_main.png)
</details>

<details><summary><strong>[Image] com.application.librarymanagement.book</strong></summary>

![](readme-images/uml_book.png)
</details>

<details><summary><strong>[Image] com.application.librarymanagement.borrow</strong></summary>

![](readme-images/uml_borrow.png)
</details>

<details><summary><strong>[Image] com.application.librarymanagement.user</strong></summary>

![](readme-images/uml_user.png)
</details>

<details><summary><strong>[Image] com.application.librarymanagement.utils</strong></summary>

![](readme-images/uml_utils.png)
</details>

## Core Features

### Dashboard
- View recommended books
- View a list of the most borrowed books
- View statistics on the number of borrows in the last 30 days

<details><summary><strong>[Image] Dashboard</strong></summary>

![](readme-images/dashboard.png)
</details>

### Book Search
Search for books using the Google Books API by title, authors, publisher, category, [ISBN](https://en.wikipedia.org/wiki/ISBN) identifier, [LCCN](https://en.wikipedia.org/wiki/Library_of_Congress_Control_Number) identifier, and [OCLC](https://en.wikipedia.org/wiki/OCLC#Identifiers_and_linked_data) identifier

<details><summary><strong>[Image] Book Search</strong></summary>

![](readme-images/book_search.png)
</details>

### Book Details
- View detailed information about a selected book
- Copy the link to Google Books for quick reference
- Generate a QR code linking to the Google Books page for mobile access
- Admins can manage stock quantities with simple add/remove controls
- Members can request to borrow the selected book

<details><summary><strong>[Image] Book Details – Admin View</strong></summary>

![](readme-images/book_details_admin.png)
</details>

<details><summary><strong>[Image] Book Details – Member View</strong></summary>

![](readme-images/book_details_member.png)
</details>

<details><summary><strong>[Image] Book Details – View QR Code</strong></summary>

![](readme-images/book_details_view_qr.png)
</details>

### Borrows
- View personal borrow records (for members) or all borrow records (for admins)
- Filter by borrow status: REQUESTED, BORROWED, RETURNED, or CANCELED
- (Admins only) Filter by username

<details><summary><strong>[Image] Borrows – Member View – All Borrows</strong></summary>

![](readme-images/borrows_member_all.png)
</details>

<details><summary><strong>[Image] Borrows – Member View – Filtered</strong></summary>

![](readme-images/borrows_member_filtered.png)
</details>

<details><summary><strong>[Image] Borrows – Admin View – Filtered</strong></summary>

![](readme-images/borrows_admin_filtered.png)
</details>

### Users Management
- View all users' information (username, name, email, role and borrows count). Members can only view their and admins' information
- (Admins only) Filter users by username
- (Admins only) Add a new user

<details><summary><strong>[Image] Users Management – Admin View</strong></summary>

![](readme-images/users_admin_view.png)
</details>

<details><summary><strong>[Image] Users Management – Admin View – Filtered</strong></summary>

![](readme-images/users_admin_view_filtered.png)
</details>

<details><summary><strong>[Image] Users Management – Admin View – Add a new user</strong></summary>

![](readme-images/users_admin_view_addnewuser.png)
</details>

<details><summary><strong>[Image] Users Management – Member View</strong></summary>

![](readme-images/users_member_view.png)
</details>

### Account Settings
Users can change their name, their email as well as their password

<details><summary><strong>[Image] Account Settings</strong></summary>

![](readme-images/account_settings.png)
</details>

## Tech Stack
- **Java** – Core application logic and API integration
- **Google Books API** – Retrieving and displaying book data
- **JavaFX** – User interface development and event handling
- **FXML** – Structuring UI layout separately from Java code
