function reply_click()
{
    alert("www");
}
function myFunction()
{
    document.getElementById("50").style.backgroundColor = "red !important";
}
var isChecked = false;
var pieceChecked;
var pieceText = "";
var pieceID;
var jobj = "";

//Richiedo la chessboard assumendo che l'html abbia soltando <div class="chessboard"></div> con nulla all'interno
//ricevo direttamente la stringa html
function RequestChessboard()
{
	var formData = { 'code': 'SETUP' };
	$.ajax(
	{
		url: "localhost:8080/STGI/game",
		type: "post",
		data: formData,
		success: function(d)
		{
			
			if(d == null || d=="")
			{
				alert("Errore nella tabella ricevuta");
			}
			else
			{
				$( ".chessboard" ).append( d );
				//solo dopo che la scacchiera è stata composta posso registrare le callback dei click sulle singole caselle
				RegisterCallbacks();	//DA TESTARE: è possibile che occorra aspettare qualche ms prima di registrare le callback perché magari l'append viene eseguita in modo asincrono	
			}
			
		},
		error: function (err) {
			alert("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}

$(window).load(function (){
	RequestChessboard();
});

function RegisterCallbacks(){
    $(".chessboard div").click(function(event) {    //evento click su un tassello di gioco
        event.preventDefault();
        if(isChecked == true)
        {
			if(this.id == pieceID)
			{
				$(this).removeClass("bg_yellow");
				for(var i = 0; i < jobj.length; ++i)
				{
					var element = document.getElementById(jobj[i].coord);
					$(element).removeClass("bg_green");
				}
				isChecked = false;
			}
			else if($(this).hasClass("bg_green"))
			{
					//invio la mossa al server
					//alert(this.id);
					//faccio il parsing del json
					//if(json.response == 0)
					pieceChecked.removeClass("white"); //DINAMICA
					pieceChecked.removeClass("bg_yellow");
					$(this).addClass("white"); //DINAMICA
					$(this).html(pieceText);
					$(pieceChecked).html("");
					for(var i = 0; i < jobj.length; ++i)
					{
						var element = document.getElementById(jobj[i].coord);
						$(element).removeClass("bg_green");
					}
					isChecked = false;
					/*if(json.enroque == 1)
					{
						var in = json.enroque
					}*/
					//else server error
			}
		}
		else if($(this).hasClass("white")) //DINAMICA
        {
			isChecked = true;
			pieceChecked = $(this);
			pieceID = this.id;
			pieceText = $(this).text();
			
			var coord = (this.id).split("",2);
			var formData = { 'code': 0, 'cL': coord[0], 'cN' : coord[1] };
				$.ajax(
				{
					url: "localhost:8080/STGI/game",
					type: "post",
					data: formData,
					success: function(d)
					{
						jobj = JSON.parse(d);
						
						if(jobj == null || jobj.length == 0)
						{
							alert("Nessuna mossa disponibile per questa pedina");
						}
						else
						{
							for(var i = 0; i < jobj.length; ++i)
							{
								$("#"+jobj[i].coord).addClass("bg_green");
							}
						}
					},
					error: function (err) {
						alert("AJAX error in request: " + JSON.stringify(err, null, 2));
					}
				});
			
			



			//QUERY AL SERVER, RICEVO UN JSON
			/*COORDINATE PER IL SERVER
				 * var coord = (this.id).split("",2);
				var let = coord[0];
				var num = coord[1];*/
			//if(response == 0)
			//non puoi muovere questa pedina
			//else
			
			/*$(this).addClass("bg_yellow");
			
                    
			var v = '[{ "coord": "F4" }, { "coord": "D5" }, { "coord": "A6" }]';
			jobj = JSON.parse(v);
			var npos = jobj.length;
			for(var i = 0; i < npos; ++i)
			{
				var element = document.getElementById(jobj[i].coord);
				$(element).addClass("bg_green");
			}*/
		}
        
		
		
        
        
        /*if(pedinaDragged == true )   //se sto trascinando una pedina la poggio 
        {
            if($(this).attr("id") != oldPedinaElement.attr("id")){ //... ovunque ma non sullo stesso posto
                //alert("voglio poggiare: " + artPedinaDragged);
                $(this).html(artPedinaDragged);
                oldPedinaElement.html("");
            }
            oldPedinaElement.removeClass("bg_green");
            pedinaDragged = false;
        }
        else    //altrimenti permetto di prendere sole pedoni bianchi
        {
            if($(this).hasClass("white"))   //check colore pedina
            {
                //alert("click su pedina bianca");
                if($( this ).hasClass("pedone"))    //check tipologia pedina
                {
                    pedinaDragged = true;
                    //alert("click su pedone");
                    tipoPedinaDragged = "pedone";
                    colorePedinaDragged = "white";
                    artPedinaDragged = $(this).text();
                    oldPedinaElement = $(this);
                    $(this).addClass("bg_green");
                }
            }
        }*/

    });
};
