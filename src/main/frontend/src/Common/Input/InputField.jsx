import React, { useEffect, useState } from 'react'
import { useNavigate } from "react-router-dom";
import styles from "./InputField.module.css";
import SearchIcon from "../../assets/images/search/search.svg";

const InputField = ({ q = "" }) => {

  const [search, setSearch] = useState("");

  const nav = useNavigate();

  useEffect(() => {
    setSearch(q || "");
  }, [q]);

  const onChangeSearch = (e) => {
    setSearch(e.target.value);
  };

  const onKeyDown = (e) => {
    if (e.keyCode === 13) {
      onClickSearch();
    }
  };

  const onClickSearch = () => {
    if (search !== "") {
      nav(`/search?q=${search}`);
    }
  };

  return (
    <div className={styles.Wrapper}>
      <div className={styles.SearchContainer}>
        <input
          className={styles.SearchBar}
          value={search}
          onKeyDown={onKeyDown}
          onChange={onChangeSearch}
          placeholder="제목/저자를 입력해주세요!"
        />
        <button className={styles.SearchButton} onClick={onClickSearch}>
          <img src={SearchIcon} alt="검색 아이콘" />
        </button>
      </div>
    </div>
  ); 
};

export default InputField;