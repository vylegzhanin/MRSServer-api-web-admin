<%@ page import="com.simagis.mrss.admin.web.ui.ptp.CPT" %>
<%@ page import="com.simagis.mrss.admin.web.ui.ptp.DTPC" %>
<%@ page import="com.simagis.mrss.admin.web.ui.ptp.DTPCs" %>
<%@ page import="com.vaadin.icons.VaadinIcons" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    final String optionBox = VaadinIcons.THIN_SQUARE.getHtml();
    DTPC data = DTPCs.INSTANCE.get(request.getParameter("id"));
    if (data == null) {
        if (request.getParameter("debug") != null) {
            data = new DTPC("payer", "date", "age", "gender", "DX",
                    Collections.singletonList(new CPT("code", "description"))
            );
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }
%>
<html>
<head>
    <title>Diagnostic Testing Pre-Certification Form</title>
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

        .cell1pc {
            display: table-cell;
            width: 10%;
        }

        .cellU10pc {
            display: table-cell;
            width: 10%;
            border-bottom: 1px solid black;
        }

        .cellU50pc {
            display: table-cell;
            width: 50%;
            border-bottom: 1px solid black;
        }

        .cellU20pc {
            display: table-cell;
            width: 20%;
            border-bottom: 1px solid black;
        }

        .cellU100pc {
            display: table-cell;
            width: 100%;
            border-bottom: 1px solid black;
        }
    </style>
</head>
<body>
<div style="margin: 0 auto; width: 9in">
    <div style="text-align: center;">
        <h1><%=data.getPayerText()%><br>
            Diagnostic Testing Pre-Certification Form</h1></div>

    <strong>Date:</strong> <%=data.getDateText()%>
    <hr>

    <div class="table100pc">
        <strong class="cell1pc">Physician’s&nbsp;Name:</strong><span class="cellU50pc"></span>
        <strong class="cell1pc" style="text-align: right;">NPI:</strong><span class="cellU50pc"></span>
    </div>

    <div class="table100pc">
        <strong class="cell1pc">Phone&nbsp;Number:</strong><span class="cellU100pc"></span>
    </div>

    <div class="table100pc">
        <strong class="cell1pc">Patient&nbsp;Name</strong><span class="cellU20pc"></span>
        <strong class="cell1pc">Insurance&nbsp;ID&nbsp;Number</strong><span class="cellU20pc"></span>
        <strong class="cell1pc">Date&nbsp;of&nbsp;Birth</strong><span class="cellU20pc"></span>
    </div>
    <br>

    <strong>Patient Age:</strong> <%=data.getPatientAgeText()%>
    <strong>Patient Gender:</strong> <%=data.getPatientGenderText()%>
    <hr>

    <h3>Diagnostic Code (ICD-10)</h3>
    <strong><%=data.getDxText()%>
    </strong>
    <hr>

    <h3>Procedures (CPT Codes) :</h3>
    <table>
        <% for (CPT cpt : data.getCptCodes()) { %>
        <tr>
            <td valign="top"><%=cpt.getCode()%>
            </td>
            <td valign="top"><%=cpt.getDescription()%>
            </td>
        </tr>
        <% } %>
    </table>
    <hr>

    Facility: <sup>(Must be In-Network to receive In-Network Benefits)</sup><br>
    <div style="padding-left: .5in">
        <div class="table100pc">
            <span class="cell"><%=optionBox%> Provider’s Office</span>
            <span class="cell"><%=optionBox%> Same Day Surgery Unit</span>
            <span class="cell"><%=optionBox%> Free-Standing Facility</span>
        </div>
        <div class="table100pc">
            <span class="cell">Name&nbsp;of&nbsp;Facility:</span>
            <span class="cellU100pc"></span></div>
        <div class="table100pc">
            <span class="cell">Address:</span>
            <span class="cellU100pc"></span></div>
        <div class="table100pc">
            <span class="cell1pc">City:</span><span class="cellU20pc"></span>
            <span class="cell1pc">State:</span><span class="cellU20pc"></span>
            <span class="cell1pc">ZIP:</span><span class="cellU10pc"></span>
            <span class="cell1pc">Phone&nbsp;Number:</span><span class="cellU20pc"></span>
        </div>
    </div>
    <hr>

    <div class="table100pc">
        <span class="cell">Urgency Status:</span>
        <span class="cell"><%=optionBox%> Elective</span>
        <span class="cell"><%=optionBox%> Urgent</span>
        <span class="cell"><%=optionBox%> Emergent</span>
    </div>

    <div class="table100pc">
        <span class="cell">Prior&nbsp;Outpatient&nbsp;Treatment:</span>
        <span class="cellU100pc"></span></div>
    <hr>

    <div class="table100pc">
        <span class="cell">PCP&nbsp;Name&nbsp;(if&nbsp;applicable)</span>
        <span class="cellU100pc"></span></div>
    <hr>

    <div style="text-align: center;">Pre-Certification Department<br>
        Fax Number: 732-562-1023
        PPO 800-992-6613 (Phone) HMO Network 800-254-0130 (Phone)
    </div>

    <div style="text-align: center;"><u>Check Benefits</u><br>
        Some Groups Do Not Provide Coverage at Non-Participating Facilities
    </div>

</div>
</body>
</html>
