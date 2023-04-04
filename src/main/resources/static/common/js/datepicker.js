let els = document.getElementsByClassName("datepicker");

Array.prototype.forEach.call(els, function(el) {
    new Pikaday({
        field: el,
        toString(date, format) {
            return moment(date).format('YYYY-MM-DD');
        },

        onOpen: function(date) {
            const restButton = document.createElement("a");
            restButton.setAttribute("role", "button");
            restButton.classList.add("btn-reset");
            restButton.classList.add("jjgray");
            restButton.textContent = "날짜 초기화";
            var _this = this;
            this.el.querySelector(".pika-lendar").appendChild(restButton);
            this.el.addEventListener("click", function(e) {
                if (e.target.classList.contains("btn-reset")) {
                    _this.clear();
                }
            });
        },
        i18n: {
            months      : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            weekdays    : ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
            weekdaysShort   :['일', '월', '화', '수', '목', '금', '토']
        }
    });
})

