Set email = CreateObject("CDO.Message")
 
email.Subject = "AutomATAthon Report"

email.From = "automationata01@gmail.com"

email.To = "automationata01@gmail.com"

Set fso = CreateObject("Scripting.FileSystemObject")

Set fs = CreateObject("Scripting.FileSystemObject")
Set MainFolder = fs.GetFolder("C:\Users\sachinke\OneDrive - AMDOCS\Projects\PLDT\Salesforce_Automation\SAF\Reports")
For Each fldr In MainFolder.SubFolders
    ''As per comment
    If fldr.DateLastModified > LastDate Or IsEmpty(LastDate) Then
        LastFolder = fldr.Name
        LastDate = fldr.DateLastModified
    End If
Next

'MsgBox LastFolder

filePath = "C:\Users\sachinke\OneDrive - AMDOCS\Projects\PLDT\Salesforce_Automation\SAF\Reports\" & LastFolder & "\TestSuiteReport.html"

'MsgBox filePath

email.AddAttachment filePath

'CurrentDirectory = fso.getAbsolutePathName(".")

Set f = fso.OpenTextFile(filePath,1)

Bodytext = f.ReadAll
f.Close
Set f = Nothing
Set fso = Nothing


email.HTMLBody = "Please find below the automation status report.<br><br>" & BodyText & "Thanks."
 
email.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/sendusing")=2
email.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpserver") = "smtp.gmail.com"
email.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpserverport")=465
email.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpusessl")=True
email.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpauthenticate")=1
email.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/sendusername")="automationata01@gmail.com"
email.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/sendpassword")="@amdocs01"

email.Configuration.Fields.Update
email.Send
set email = Nothing