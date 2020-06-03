var booking_default = $(".modal-body").html();

//when load page
$(function () {
    $.get("serv", {action: "new"}, function (data) {
        console.log(data);
        setStatusPlaceSVG(data.db_places);
        setStatusPlaceSVG(data.reserved);
    }).then(poll);
});


//when pooling
function poll() {
    $.ajax({
        url: "serv", data: {action: "poll"}, success: function (data) {
            //Update your dashboard gauge
            console.log(data);
            setStatusPlaceSVG(data.db_places);
            setStatusPlaceSVG(data.reserved);
        }, dataType: "json", complete: poll, timeout: 30000
    });
};


//when click button buy open modal
$('#buy').on('click', function () {
    var places = [];

    Object.entries($("#places .chose"))
        .forEach(
            ([key, value]) => {
                if (value.id != undefined) {
                    {
                        places.push({
                            place: value.id,
                            state: 2
                        });

                    }
                }
            }
        );
    if (places.length != 0) {
        //$(".modal-body").html(BOOKING_CONTENT_DEFAULT);
        $('#booking').modal('show');
    }
    $.post("serv", {action: "chose", places: JSON.stringify(places)})
        .done(function () {
                //get ajax data for current session
                $.get("serv", {action: 'payment'})
                    .done(function (data) {
                        console.log(data);
                        result(data);

                    }).fail(function () {
                    //error msg
                });


            }
        );
    //when send data
    $('.subscribe').on('click', function () {
        let account = {};

        account.phone = $("#phone").val();
        account.name = $("#username").val();


        $.post("serv", {action: 'buy', account: JSON.stringify(account)})
            .done(function () {
                let text = `<div class="alert alert-success" role="alert">
                            <h2>Thank's for purchase!</h2></div>`;
                $(".modal-body > form:nth-child(2)").html(text);
            }).fail(function () {
            let text = `<div class="alert alert-warning" role="alert">
                            <h2>Name or phone are incorrect</h2></div>`;
            $(".modal-body > form:nth-child(2)").html(text);
        });
    })
});

function result(data) {
    console.log("in result");
    let obj = $('#calc_price');
    console.log(obj);
    obj.html('');
    let sum = 0.0;
    for (i = 0; i < data.length; i++) {
        let place = data[i].place.match(/(.+)\.(.+)/);
        let row = `
                     row:  
                    <span class="badge badge-info">` + place[1] + `</span>
                     place: 
                    <span class="badge badge-info">` + place[2] + `</span>
                     price: 
                    <span class="badge badge-info">` + data[i].price + `₽</span>
                    <br>
                    `;
        sum += data[i].price;
        obj.append(row);
    }
    obj.append(`<hr>`);
    obj.append(`<h3> total price is: <span class="badge badge-danger">` + sum + `₽</span></h3>`);
}

//when press reset
$('#reset').on('click', function () {
    $.post("serv", {action: "clear"})
        .done(function () {
                window.location.reload();
            }
        );
})

//when close modal
$('#booking').on('hide.bs.modal', function (event) {
    $(".modal-body").html(booking_default);
    close();
});

function close() {
    $.post("serv", {action: "close"});
}

