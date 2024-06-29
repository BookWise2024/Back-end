import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
// ------------------------------------------------------
import mainStyle from '../MainPage.module.css'
import AppStyle from "../../App.module.css";
//-------------------------------------------------------
import scroll from "./WithScroll.jsx"

export default function MainBookList() {
    // 정보나루 api_key(정보나루 api 문서 page7~8 참고)
    const jungbonaru_api = "3bf8f995a4d8a78bcc0dd4c2e503b06287b7f0f6dd3185e0d91c0ad715c63ad5";
    const best_take_out_url = "http://data4library.kr/api/loanItemSrch?authKey=" + jungbonaru_api;

    // const navigate = useNavigate();
    const baseUrl = "http://localhost:8080";

    // 추천 책 리스트
    const [list, setList] = useState([]);
    const [user, setUser] = useState([]);
    // 최상단 추천 리스트
    const bookList = [];
    const bookElements = [];
                
    useEffect(() => {
        // 로그인 여부 확인
        const login_check = async() => {
            try {
                const response = await axios.get(baseUrl + "/check");
                console.log(response.data);
                // 유저 정보 저장
                setUser(response.data);

                // 유저 정보가 null이 아니면 정보를 이메일로 세팅
                if(user) {
                    setEmail(user.email);
                } else {
                    setEmail("로그인이 필요합니다.");
                }

            } catch (err) {
                console.log(err);
            }
        }

        // 사용자 추천 책 리스트 요청
        const userRecommend = async() => {
            try{
                const res = await axios.get(baseUrl + "/사용자 책 추천 리스트 요청 경로");
                setList(res.data);
            } catch(e) {
                console.log(e);
            }
        };


        // 도서관 추천 책 리스트 요청
        const recomend = async() => {
            try{
                const res = await axios.get(best_take_out_url);
                console.log(res.data);
                setList(res.data);
            } catch(e) {
                console.log(e);
            }
        };
        
        // login_check();
        // if(user) {
        //     userRecommend();
        // }
        recomend();
    },[]);
    // ---------------------------------------------------------------------------
    // 로그인 여부에 따른 상단의 추천 도서 종류 변경
    // if(user) {
    //     // 사용자 추천 책 top 10
    //     bookList.push(
    //         <>
    //             <div className={AppStyle.subtitle2}>
    //                 { user.userName }님을 위한 맞춤 추천
    //             </div>
    //             <div className={mainStyle.list_container}>
    //                 { bookElements }
    //             </div>
    //         </>
    //     );
    //     for(let i = 0; i < 10; i++) {
    //         bookList.push(
    //                 <>
    //                 <img
    //                 style={{ width: "10.1875rem", height: "14.0625rem", borderRadius: "0.25rem" }}
    //                 src={ list[i].image }/>
    //                 </>
    //         )
    //     }
    // } else {
        // 추천 책 top 10
        // for(let i = 0; i < 10; i++){
        //     bookElements.push(
        //         <>
        //             <img
        //                 key={i}
        //                 style={{ width: "10.1875rem", height: "14.0625rem", borderRadius: "0.25rem" }}
        //                 src={list[i].bookImageURL}
        //             />
        //         </>
        //     )
        // }

        bookList.push(
            <>
                <div key="title" className={AppStyle.subtitle2}>
                    지금 뜨는 도서
                </div>
                <div key="element" className={mainStyle.list_container}>
                    { bookElements }
                </div>
            </>
        );
    // }
    // ---------------------------------------------------------------------------
    
    return(
        <>
            { bookList }
        </>
    );
}