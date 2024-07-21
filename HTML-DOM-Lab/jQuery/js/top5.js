$(document).ready(function(){
  $("#cds").children("img").click(addToTop5);   
  	// 또는 $("#cds > img").click(addToTop5);
  
  $(":button").click(startOver);
});

function addOnClickHandlers() {
/*
  var cdsDiv = document.getElementById("cds");
  var cdImages = cdsDiv.getElementsByTagName("img");
  for (var i=0; i<cdImages.length; i++) {
    cdImages[i].onclick = addToTop5;
  }
*/
  $("#cds>img").click(addToTop5);
}

function addToTop5() {
  var imgElement = this;
/*
  var top5Element = document.getElementById("top5");
  var numCDs = 0;
  for (var i=0; i<top5Element.childNodes.length; i++) {
    if (top5Element.childNodes[i].nodeName.toLowerCase() == "img") {
      numCDs = numCDs + 1;
    }
  }
*/  
  var numCDs = $("#top5").children("img").length;
  	// 또는 var numCDs = $("#top5 > img").length;

  if (numCDs >= 5) {
    alert("You already have 5 CDs. Click \"Start Over\" to try again.");
    return;
  }
/*
  top5Element.appendChild(imgElement);
  imgElement.onclick = null;
  
  var newSpanElement = document.createElement("span");
  newSpanElement.className = "rank";
  var newText = document.createTextNode(numCDs + 1);
  newSpanElement.appendChild(newText);
  top5Element.insertBefore(newSpanElement, imgElement);
*/
  $("#top5").append(imgElement);
  $(imgElement).off("click");   
  $(imgElement).click(moveToOriginalPosition); // 선택된 이미지를 제자리로 복귀(이동)시킴
  
  var newSpanElement = document.createElement("span");
  $(newSpanElement).addClass("rank");
  $(newSpanElement).text(numCDs + 1);
  $(newSpanElement).insertBefore(imgElement);  
    // 또는 $(imgElement).before(newSpanElement);  
  
  // 또는 아래와 같이 구현 가능
/*
  var newSpanElement = "<span></span>";
  $(newSpanElement).addClass("rank")
  				.text(numCDs + 1)
  				.insertBefore(imgElement);
*/
}

function startOver() {
/*  
  var top5Element = document.getElementById("top5");
  var cdsElement = document.getElementById("cds");
  while (top5Element.hasChildNodes()) {
    var firstChild = top5Element.firstChild;
    if (firstChild.nodeName.toLowerCase() == "img") {
      cdsElement.appendChild(firstChild);
    } else {
      top5Element.removeChild(firstChild);
    }
  }
  addOnClickHandlers();
*/
  $("#top5").children("img").each( 
  	// 또는  $("#top5 > img").each( 
	function() {
	  $(this).appendTo("#cds");	  
	  $(this).off("click");
	  $(this).on("click", addToTop5);
	}
  );  
  $("#top5").children().remove();  
  
  // 또는 다음과 같이 구현 가능
/*
  $("#top5>img").appendTo("#cds")	// 위치 이동 
				.off("click")		// click 이벤트 핸들러 제거
				.on("click", addToTop5);  // click 이벤트 핸들러 추가
  $("#top5").children().remove();	// 모든 <span> 삭제  
*/
}

function moveToOriginalPosition() { // 선택된 top5 아래의 하나의 이미지를 제자리로 이동시킴
	var imgElement = this; 		// 선택된 <img> 엘리먼트
	
	// imgElement의 순위를 나타내는 <span>을 삭제
	$(imgElement).prev().remove();
	
	var nextSpans = $(imgElement).nextAll("span");
	
	// cds의 <img>들 중 id 값이 imgElement의 id 값보다 크면서 가장 가까운 것을 찾아
	var ind = -1;
    $("#cds > img").each( function(i) { // <img>들에 대해 순서대로 funciton 호출; i는 각 <img>의 인덱스  
		var curImg = this; // this는 function이 호출된 <img>
		if(Number(curImg.id) > Number(imgElement.id)){// imgElement를 삽입할 다음 형제 <img> 발견
			if(ind == -1) ind = i;  // <img>의 인텍스를 ind에 저장
		}
	});
	
	if(ind == -1){  // imgElement의 다음 형제 <img>가 발견되지 않았음
		$("#cds").append(imgElement);// cds의 마지막 자식으로 추가
	}
	else{
		$("#cds>img").eq(ind).before(imgElement);// imgElement를 ind 인덱스에 해당하는 <img> 앞에 삽입
	}
	
	if (parseInt(closestElement.attr("id")) < cdsImages.length) {
        closestElement.after(imgElement);
    } else {
        closestElement.before(imgElement); // 부모 요소에 추가
    }
    /*

  	// 위 코드의 더 효율적인 구현 방법  
 	// cds의 <img>들 중 id 값이 imgElement의 id 값보다 크면서 가장 가까운 것을 찾음
	 $("#cds > img").each( function(){// <img>들에 대해 순서대로 funciton 호출 
	    var curElement = this;// this는 function이 호출된 <img>
	    if (Number(imgElement.id) < Number(curElement.id)) {// imgElement를 삽입할 다음 형제 <img> 발견 
	         $(imgElement).insertBefore(curElement);// imgElement를 curImg 앞에 삽입
	         return false;// false return: each()의 반복문 실행이 중단됨 (다음 <img>들은 무시)
	    }
	 });
	
	  // cds의 <img>들 중 id 값이 imgElement보다 큰 것이 없을 경우 위에서 처리되지 않으므로 별도로 처리
	  if ($(imgElement).parent().attr("id") == "top5") {// imgElement가 아직 top5 아래에 위치
	      $("#cds").append(imgElement);// cds의 마지막 자식으로 추가
	  }
	 */

	// top5 아래의 imgElement 다음에 있었던 이미지들의 순위 값을 변경
  	$("#top5 > span").each( function(i){// i: index
       $(this).text(i + 1);
  	});

  	// imgElement에 대해 onclick 이벤트 핸들러 재설정
  	$(imgElement).off("click").on("click", addToTop5);

  	// top5 아래의 이미지들의 순위 값 변경
	 $("#top5 > span").each( function(i){// i: index 값 (0, 1, 2, ...)
	     $(this).text(i + 1);                           // 1, 2, 3, ...
	 });
	 
	 /*
	 // 또는 아래와 같이 top5 아래의 imgElement 다음에 있었던 이미지들의 순위 값만 변경
	 $(nextSpans).each( function() {
	   $(this).text($(this).text() - 1);
	 }); 
	 */
			
}
