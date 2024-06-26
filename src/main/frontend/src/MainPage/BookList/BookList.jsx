import style from "./BookList.module.css";
import Book1 from "../assets/img/book/book1.png";
import Book2 from "../assets/img/book/book2.png";
import Book3 from "../assets/img/book/book3.png";
import Book4 from "../assets/img/book/book4.png";
import AppStyle from "../App.module.css";

const BookList = () => {
  return (
    <div className={style.container}>
      <div className={AppStyle.subtitle2}
      >
        지금 뜨는 도서
      </div>
      <div className={style.list_container}>
        <img
          style={{ width: "6.1875rem", height: "8.875rem", borderRadius: "0.25rem" }}
          src={Book1}
          alt="책1"
        />
        <img
          style={{ width: "6.1875rem", height: "8.875rem", borderRadius: "0.25rem" }}
          src={Book2}
          alt="책2"
        />
        <img
          style={{ width: "6.1875rem", height: "8.875rem", borderRadius: "0.25rem" }}
          src={Book3}
          alt="책3"
        />
        <img
          style={{width: "6.1875rem", height: "8.875rem", borderRadius: "0.25rem" }}
          src={Book4}
          alt="책4"
        />
      </div>
    </div>
  );
};

export default BookList;
