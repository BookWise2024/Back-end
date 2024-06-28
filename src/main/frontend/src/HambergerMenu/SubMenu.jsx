import { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios'
import styles from './SubMenu.module.css'
import layout from '../Common/Layout.module.css'
import img from '../assets/images/menu/book_icon_black.svg'
import img2 from '../assets/images/menu/library.svg'
import logoutimg from '../assets/images/menu/logout.svg'

export default function SubMenu() {
    const [email, setEmail] = useState("로그인이 필요합니다");
    const [user, setUser] = useState([]);

    const title = [
        {key: 1, name: '내 선호책 리스트', image: img}, 
        {key: 2, name: '공공 도서관 찾기', image: img2}
    ];
    const lis = [];
    const log = [];

    const baseUrl = "http://localhost:8080";
    // 로그인 여부 확인
    useEffect(() => {
        const login_check = async() => {
            try {
                const response = await axios.get(baseUrl + "/check");
                console.log(response.data);
                // 유저 정보 저장
                setUser(response.data);

                // 유저 정보가 null이 아니면 정보를 이메일로 세팅
                if(user) {
                    setEmail(user.userEmail);
                } else {
                    setEmail("로그인이 필요합니다.");
                }

            } catch (err) {
                console.log(err);
            }
        }

        login_check();
    }, []);

    // 로그아웃
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post(baseUrl + "/logout", {}, { withCredentials: true });
            navigate('/');
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    // 메뉴 리스트 나열
    for(let i = 0; i < title.length; i++) {
        lis.push(
            <div key={ title[i].key } className={ styles.menulist }>
                <div className={ styles.menuicon }>
                    <img className={ styles.icon } src={title[i].image}/>
                </div>
                <div className={ styles.menutext }>{ title[i].name }</div>
            </div>
        );
    }

    // 로그인 여부에 따른 로그아웃 버튼 유무
    if(user) {
        log.push(
            <div key="logout" className={ styles.logoutbutton } onClick={handleLogout}>
                <div className={ styles.logouticon }>
                    <img className={ styles.secicon } src={ logoutimg }/>
                </div>
                <div className={ styles.menutext }>로그아웃</div>
            </div>
        )
    }
    // -----------------------------------------------------------------------
    return(
        <div className={ layout.oplayout }>
            <div className={ styles.sublayout }>
                <div className={ styles.identity } onClick={ (e) => {
                    if(!user){
                        window.location.href = "http://localhost:5173/login";
                    }
                } }>{ email }</div>
                <div className={ styles.divide }></div>
                <div className={ styles.submenu }>
                    {lis}
                </div>
                {log}
            </div>
        </div>
    )
}

