(function (window, undefined) {
    var Business = Base.getClass('main.util.Business');

    Base.ready({
        initialize: fInitialize
    });

    function fInitialize() {
        Business.followUser();
    }
})();
console.debug("执行了follow.js");