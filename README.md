# Library Management System [![starline](https://starlines.qoo.monster/assets/qoomon/starlines)](https://github.com/brownfox2k6/LibraryManagementSystem)

- **Course:** Object-oriented programming
- **Course code:** 2425H_INT2204_60
- **Semester:** Summer 2024–2025

## Members
- **24021400 Nguyễn Trọng Đại**
- **24021408 Lục Thị Diệp**

## Brief description

An application that helps users search for books, view detailed information, and manage borrowing activities. It integrates with the Google Books API for accurate book data, and supports QR code generation for quick access. Members can request to borrow books and track their borrowing history, while admins can manage stock and monitor all borrow records.

## Core features
### Dashboard
- View recommended books
- View a list of the most borrowed books
- View statistics on the number of borrows in the last 30 days

<details><summary><strong>[Image] Dashboard</strong></summary>

![](README_images/dashboard.png)
</details>

### Book search
Search for books using the Google Books API by title, authors, publisher, category, [ISBN](https://en.wikipedia.org/wiki/ISBN) identifier, [LCCN](https://en.wikipedia.org/wiki/Library_of_Congress_Control_Number) identifier, and [OCLC](https://en.wikipedia.org/wiki/OCLC#Identifiers_and_linked_data) identifier

<details><summary><strong>[Image] Book search</strong></summary>

![](README_images/book_search.png)
</details>

### Book details
- View detailed information about a selected book
- Copy link to Google Books for quick reference
- Generate a QR code linking to Google Books page for mobile access
- Admins can manage stock quantity with simple add/remove controls
- Members can request to borrow the selected book

<details><summary><strong>[Image] Book details - Admin's view</strong></summary>

![](README_images/book_details_admin.png)
</details>

<details><summary><strong>[Image] Book details - Member's view</strong></summary>

![](README_images/book_details_member.png)
</details>

<details><summary><strong>[Image] Book details - View QR code</strong></summary>

![](README_images/book_details_view_qr.png)
</details>

### Borrow
- View personal borrows (for members) or view all borrows (for admins)
- Filter by borrow status: REQUESTED, BORROWED, RETURNED or CANCELED
- (Admins only) Filter by username

<details><summary><strong>[Image] Borrow - Member's view - All borrows</strong></summary>

![](README_images/borrow_member_all.png)
</details>

<details><summary><strong>[Image] Borrow - Member's view - Filtered</strong></summary>

![](README_images/borrow_member_filtered.png)
</details>

<details><summary><strong>[Image] Borrow - Admin's view - Filtered</strong></summary>

![](README_images/borrow_admin_filtered.png)
</details>

## Tech stack
- **Java** - Core application logic and API integration
- **JavaFX** - User interface development and event handling
- **CSS** - Styling and customizing JavaFX components
- **Google Books API** - Retrieving and displaying book data
- **FXML** - Structuring UI layout separately from Java code