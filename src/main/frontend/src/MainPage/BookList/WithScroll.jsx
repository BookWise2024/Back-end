const WidthScroll = ({ children }) => {
    let slider = "";
    if (typeof document !== "undefined") {
      slider = document.querySelector(".items");
    }
  
    let isDown = false;
    let startX;
    let scrollLeft;
  
    if (slider) {
      slider.addEventListener("mousedown", (e) => {
        isDown = true;
        slider.classList.add("active");
        startX = e.pageX - slider.offsetLeft;
        scrollLeft = slider.scrollLeft;
  
        // 나머지 경우에는 마우스 포인터 삭제
        slider.classList.remove("pointer");
      });
  
      slider.addEventListener("mouseleave", () => {
        isDown = false;
        slider.classList.remove("active");
  
        // 나머지 경우에는 마우스 포인터 삭제
        slider.classList.remove("pointer");
      });
  
      slider.addEventListener("mouseup", () => {
        isDown = false;
        slider.classList.remove("active");
  
        // 나머지 경우에는 마우스 포인터 삭제
        slider.classList.remove("pointer");
      });
  
      slider.addEventListener("mousemove", (e) => {
        if (!isDown) return;
        e.preventDefault();
        const x = e.pageX - slider.offsetLeft;
        const walk = x - startX;
        slider.scrollLeft = scrollLeft - walk;
  
        // 마우스를 눌러서 드래그 스크롤 할 때만 cursor pointer 설정
        slider.classList.add("pointer");
      });
    }
  
    return <div>{children}</div>;
  };
  
  export default WidthScroll;