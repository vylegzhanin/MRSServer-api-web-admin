<%@ page import="com.simagis.mrss.admin.web.ui.ptp.ABN" %>
<%@ page import="com.simagis.mrss.admin.web.ui.ptp.ABNs" %>
<%@ page import="com.vaadin.icons.VaadinIcons" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    final String optionBox = VaadinIcons.THIN_SQUARE.getHtml();
    final ABN data = ABNs.INSTANCE.get(request.getParameter("id"));
    if (data == null) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
%>
<html>
<head>
    <title>Advance Beneficiary Notice of Noncoverage (ABN)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/VAADIN/themes/valo/styles.css?v=8.0.6"/>
    <style>
        table {
            border-collapse: collapse;
            margin-top: 5px;
            margin-bottom: 5px;
        }

        td {
            padding: 5px;
            font-family: Arial, sans-serif;
        }

        body {
            font-family: Arial, sans-serif;
        }

        .table100pc {
            display: table;
            width: 100%
        }

        .cell {
            display: table-cell;
        }

        .cellU100pc {
            display: table-cell;
            width: 100%;
            border-bottom: 1px solid black;
        }

        .value {
            font-weight: bold;
        }

        .valueU {
            font-weight: bold;
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div style="margin: 0 auto; width: 9in">
    <h3>Notifier: BioReference Laboratories Inc.</h3>
    <h3 class="table100pc">
        <span class="cell">PatientName:</span>
        <span class="cellU100pc"></span></h3>
    <h3 class="table100pc">
        <span class="cell">Identification&nbsp;Number:</span>
        <span class="cellU100pc"></span></h3>
    <hr>
    <div style="text-align: center;"><h1>Advance Beneficiary Notice of Noncoverage (ABN)</h1></div>

    <strong>NOTE:</strong> If Payer doesn't pay for diagnostic test: <span class="valueU"><%=data.getTestText() %></span>
    below, you may have to pay. Payer does not pay for everything, even some care that you or your health care
    provider have good reason to think you need. We expect Payer may not pay for the diagnostic test: <span
        class="valueU"><%=data.getTestText() %></span> below.
    <table width="100%" border="1">
        <tr>
            <td bgcolor="#d3d3d3" nowrap><strong>Diagnostic Test</strong></td>
            <td bgcolor="#d3d3d3"><strong>Reason Payer May Not Pay for the test or its components:</strong></td>
            <td bgcolor="#d3d3d3" nowrap><strong>Estimated Cost</strong></td>
        </tr>
        <tr>
            <td valign="top" nowrap><span class="value"><%=data.getTestText() %></span></td>
            <td valign="top"><span class="value"><%=data.getReasonText() %></span></td>
            <td valign="top" nowrap><span class="value"><%=data.getTestExpectFee() %></span></td>
        </tr>
    </table>

    <h2>WHAT YOU NEED TO DO NOW:</h2>
    <ul>
        <li>Read this notice, so you can make an informed decision about your care.</li>
        <li>Ask us any questions that you may have after you finish reading.</li>
        <li>Choose an option below about whether to receive the diagnostic test: <span
                class="valueU"><%=data.getTestText() %></span> listed above.<br>
            <strong>Note:</strong> If you choose Option 1 or 2, we may help you to use any other insurance that you
            might have, but Payer cannot require us to do this.
        </li>
    </ul>

    <table width="100%" border="1">
        <tr>
            <td bgcolor="#d3d3d3"><strong>G. OPTIONS: Check only one box. We cannot choose a box for you.</strong>
            </td>
        </tr>
        <tr>
            <td>
                <strong><%=optionBox%> OPTION 1.</strong> I want the diagnostic test: <span
                    class="valueU"><%=data.getTestText() %></span> listed above. You may ask to be paid now, but I also
                want Payer billed for an official decision on payment, which is sent to me on a Payer Summary
                Notice (MSN). I understand that if Payer doesn't pay, I am responsible for payment, but <strong>I can
                appeal to Payer</strong> by following the directions on the MSN. If Payer does pay, you will refund
                any payments I made to you, less co-pays or deductibles.<br><br>

                <strong><%=optionBox%> OPTION 2.</strong> I want the diagnostic test: <span
                    class="valueU"><%=data.getTestText() %></span> listed above, but do not bill Payer. You may ask to be
                paid now as I am responsible for payment. <strong>I cannot appeal if Payer is not
                billed.</strong><br><br>

                <strong><%=optionBox%> OPTION 3.</strong> I don’t want the diagnostic test: <span
                    class="valueU"><%=data.getTestText() %></span> listed above. I understand with this choice I am not
                responsible for payment, and <strong>I cannot appeal to see if Payer would pay.</strong>
            </td>
        </tr>
    </table>

    <h2>Additional Information:</h2>
    <strong>This notice gives our opinion, not an official Payer decision.</strong> If you have other questions on this
    notice or Payer billing, call <strong>1-800-MEDICARE</strong> (1-800-633-4227/<strong>TTY</strong>:
    1-877-486-2048). Signing below means that you have received and understand this notice. You also receive a copy.

    <table width="100%" border="1">
        <tr>
            <td width="50%"><strong>Patient Signature:<br><br></strong></td>
            <td width="50%"><strong>Date:<br><br></strong></td>
        </tr>
    </table>

    <small>According to the Paperwork Reduction Act of 1995, no persons are required to respond to a collection of
        information unless it displays a valid OMB control number. The valid OMB control number for this information
        collection is 0938-0566. The time required to complete this information collection is estimated to average 7
        minutes per response, including the time to review instructions, search existing data resources, gather the data
        needed, and complete and review the information collection. If you have comments concerning the accuracy of the
        time estimate or suggestions for improving this form, please write to: CMS, 7500 Security Boulevard, Attn: PRA
        Reports Clearance Officer, Baltimore, Maryland 21244-1850.
        Form CMS-R-131 (03/11) Form Approved OMB No. 0938-0566
    </small>

    <hr>
    <table width="100%">
        <tr>
            <td><strong>Form CMS-R-131 (03/11)</strong></td>
            <td align="right"><strong>Form Approved OMB No. 0938-0566</strong></td>
        </tr>
    </table>
</div>
</body>
</html>
