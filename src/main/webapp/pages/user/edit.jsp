<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="sequence.common.Constants" %>

	<c:if test="${not empty error}">
	    <div class="notify">${error}</div>
	</c:if>

    <h1>Your Profile</h1>
    <p>Below are your contribution details.</p>

    <c:if test="${subscriptions.size() > 0}">
        <div id="subscription-details">
            <h3>Subscriptions</h3>

            <c:forEach var="subscription" items="${subscriptions}">
                <p><strong>${subscription.amountZero}
                        </strong> monthly to
                        <c:if test="${subscription.prospect != null}">
                            ${subscription.prospect.name}
                        </c:if>
                        <c:if test="${subscription.prospect == null}">
                            Dynamics <strong>+Gain</strong>
                        </c:if>
                        <c:if test="${!subscription.cancelled}">
                            <a href="javascript:" class="button beauty small cancel" class="button beauty small"
                                data-subscription="${subscription.stripeId}"
                               <c:if test="${subscription.prospect != null}">
                                    data-prospect="${subscription.prospect.id}"
                               </c:if>
                                >Cancel</a>
                        </c:if>
                        <c:if test="${subscription.cancelled}">
                            <strong class="yellow">Cancelled</strong>
                        </c:if>
                </p>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${charges.size() > 0}">
        <h3 style="margin-top:30px;">One-Time Donations</h3>
        <c:forEach var="charge" items="${charges}">
            <p><strong>${charge.amountZero}</strong> donated to
                <c:if test="${charge.prospect != null}">
                    ${charge.prospect.name}
                </c:if>
                <c:if test="${charge.prospect == null}">
                    Dynamics <strong>+Gain</strong>
                </c:if>
            </p>
        </c:forEach>
    </c:if>

    <c:if test="${charges.size() == 0 && subscriptions.size() == 0}">
        <p>No current donations.</p>
        <p><a href="/z/donate" class="href-dotted">Donate</a></p>
    </c:if>


    <c:if test="${charges.size() > 0 || subscriptions.size() > 0}">
        <p>Thank you for being a contributor.<br/>
        Contact us any time if you have questions.</p>
    </c:if>


    <a href="/z/user/edit_password/${user.id}" class="href-dotted" style="display:inline-block;margin-top:60px;">Update Password</a>
	


<script>
    $(document).ready(function(){
        var $cancel = $('.cancel');

        $cancel.click(function(){
            var subscription = $(this).attr('data-subscription')
            var prospect = $(this).attr('data-prospect')
            console.log(prospect)
            if(prospect != null){
                console.log('prospect exists')
                removeByLocation(prospect, subscription)
            }
            if(prospect == null){
                remove(subscription)
            }
        })

        var remove = function(subscription){
            return $.ajax({
                method: "Delete",
                url : "/z/donate/cancel/" + subscription,
                success: success,
                error : error
            })
        }

        var removeByLocation = function(prospect, subscription){
           return $.ajax({
                method: "Delete",
                url : "/z/donate/cancel/" + prospect + "/" + subscription,
                success: success,
                error : error
            })
        }

        var success = function(){
            window.prospect.reload();
        }

        var error = function(ex){
            alert(ex.toString());
        }
    });
</script>
