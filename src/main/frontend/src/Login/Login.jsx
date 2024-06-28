import { useState } from 'react'
import kakao from './Kakao.module.css'
import layout from '../Common/Layout.module.css'
import exit from '../assets/images/menu/exit_icon.svg'
import logo from '../assets/images/logo/bookwise_logo.svg'
import kakaologo from '../assets/images/login/kakao-svgrepo.svg'

export default function Login() {

    const Rest_api_key='451aea6cf5b0142c652610b32748b4e9' //REST API KEY
    const redirect_uri = 'http://localhost:5173/auth/kakao/callback' //Redirect URI
    // oauth 요청 URL
    const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${Rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`
    const handleLogin = ()=>{
        window.location.href = kakaoURL
    }

    const goBack = () => {
        location.href = 'http://localhost:5173/';
    }

    return (
        <div className={ layout.layout }>

            {/* x 버튼 화면 조정 필요 */}
            <div className={ kakao.exit }>
                <img className={ kakao.exitButton } onClick={ goBack } src = { exit }/>
            </div>

            <div className={ kakao.titleBase }>
                <div className={ kakao.subTitle }>AI 기반 도서 추천 서비스</div>
                <div className={ kakao.mainTitle }>
                    <img className={ kakao.mainLogo } src = { logo }/>
                </div>
            </div>

            <div className={ kakao.buttonBase }>
                <div className={ kakao.desc }>
                    간편하게 로그인하고 <br/> 다양한 서비스를 이용해보세요
                </div>
                <button onClick={handleLogin} className={kakao.kakaoButton}>
                    {/* image는 kakao.module.css에서 백그라운드로 설정 */}
                    <div className={kakao.buttonContent}>
                        <img className={kakao.kakaoImage} src={ kakaologo }/>
                        <div className={kakao.buttonText}>
                            카카오로 1초 만에 시작하기
                        </div>
                    </div>
                </button>
            </div>
            
        </div>
    )
}