function addOnClickHandlers() {
	var cdsDiv = document.getElementById("cds");
	var cdImages = cdsDiv.getElementsByTagName("img");
	for (var i = 0; i < cdImages.length; i++) {
		cdImages[i].onclick = addToTop5;
	}
}

function addToTop5() {
	var imgElement = this;
	var top5Element = document.getElementById("top5");
	var numCDs = 0;

	for (var i = 0; i < top5Element.childNodes.length; i++) {
		if (top5Element.childNodes[i].nodeName.toLowerCase() == "img") {
			numCDs = numCDs + 1;
		}
	}
	if (numCDs >= 5) {
		alert("You already have 5 CDs. Click \"Start Over\" to try again.");
		return;
	}

	top5Element.appendChild(imgElement);
	//imgElement.onclick = null;
	imgElement.onclick = moveToOriginalPosition; // 선택된 이미지를 제자리로 이동시킴

	var newSpanElement = document.createElement("span");
	newSpanElement.className = "rank";
	var newTextElement = document.createTextNode(numCDs + 1);
	newSpanElement.appendChild(newTextElement);
	top5Element.insertBefore(newSpanElement, imgElement);
}

function startOver() {
	var top5Element = document.getElementById("top5");
	var cdsElement = document.getElementById("cds");
	while (top5Element.hasChildNodes()) {
		var firstChild = top5Element.firstChild;
		if (firstChild.nodeName.toLowerCase() ==  "img") {
			cdsElement.appendChild(firstChild);
		} else {
			top5Element.removeChild(firstChild);
		}
	}

	addOnClickHandlers();
}

function moveToOriginalPosition() { // 선택된 이미지를 제자리로 이동시킴
	var imgElement = this; // 선택된 <img> 엘리먼트
	var cds = document.getElementById("cds");
	var cdImages = cds.getElementsByTagName("img");
	
	// cds의 <img>들 중 id 값이 imgElement의 id 값보다 크면서 가장 가까운 것을 찾음
	for (var i = 0; i < cdImages.length; i++) {
	    if (Number(cdImages[i].id) > Number(imgElement.id))
	    break;
  	}
	// imgElement의 순위를 나타내는 <span>을 삭제
	var top5Element = imgElement.parentNode;
	top5Element.removeChild(imgElement.previousSibling);
	
	var nextSpan = imgElement.nextSibling; // 다음 <span> 노드를 미리 구함
	
	// imgElement를 cds의 제 위치에 삽입 (주의: imgElement가 마지막 이미지인 경우)
	if (i == cdImages.length) {
     cds.appendChild(imgElement);
  	} else {
     cds.insertBefore(imgElement, cdImages[i]); 
  	}
	
	// imgElement에 대해 onclick 이벤트 핸들러 재설정
	imgElement.onclick = addToTop5;
	
	// top5 이미지들의 순위 값을 재계산하여 변경
	 var rank = 1;

    for (var i = 0; i < top5Element.childNodes.length; i++) {
      if (top5Element.childNodes[i].nodeName.toLowerCase() == "span") {
          top5Element.childNodes[i].innerHTML = rank; 
          rank++;
      }
   }
   /*

  // 또는 아래와 같이 top5 아래의 imgElement 다음에 있었던 이미지들의 순위 값만 변경

  for (var node = nextSpan; node != null; node = node.nextSibling) {

    if (node.nodeName.toLowerCase() == "span") {

       node.textContent = Number(node.textContent) - 1;    // 순위 값을 1 감소

    }

  }

  */
   
}
